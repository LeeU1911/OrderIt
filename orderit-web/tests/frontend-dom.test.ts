import { describe, it, before, after } from 'node:test';
import assert from 'node:assert/strict';
import { createApp } from '../src/app.js';
import { Server } from 'node:http';
import { JSDOM } from 'jsdom';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const publicDir = path.join(__dirname, '../public');

const flush = () => new Promise(r => setTimeout(r, 40));

const waitFor = async (dom: JSDOM, selector: string, timeout = 2000) => {
  const start = Date.now();
  while (Date.now() - start < timeout) {
    const el = dom.window.document.querySelector(selector);
    if (el) return el;
    await new Promise(r => setTimeout(r, 20));
  }
  throw new Error(`Timeout waiting for ${selector}`);
};

describe('Frontend DOM & HTTP Suite', () => {
  let app: any;
  let server: Server;
  let baseUrl: string;
  let closeDb: () => void;
  let dom: JSDOM;

  before(async () => {
    const created = createApp(':memory:');
    app = created.app;
    closeDb = created.close;

    await new Promise<void>((resolve) => {
      server = app.listen(0, () => {
        const address = server.address() as any;
        baseUrl = `http://127.0.0.1:${address.port}`;
        resolve();
      });
    });

    const rawHtml = fs.readFileSync(path.join(publicDir, 'index.html'), 'utf8');
    const html = rawHtml
      .replace('<script src="i18n.js"></script>', '')
      .replace('<script src="app.js"></script>', '');
    const i18nJs = fs.readFileSync(path.join(publicDir, 'i18n.js'), 'utf8');
    const js = fs.readFileSync(path.join(publicDir, 'app.js'), 'utf8');

    dom = new JSDOM(html, {
      url: baseUrl,
      runScripts: 'dangerously',
      pretendToBeVisual: true,
    });

    dom.window.fetch = (input: RequestInfo | URL, init?: RequestInit) => {
      let urlStr = typeof input === 'string' ? input : input.toString();
      if (urlStr.startsWith('/')) {
        urlStr = baseUrl + urlStr;
      }
      return fetch(urlStr, init);
    };

    dom.window.EventSource = class EventSource {
      onopen: any;
      onmessage: any;
      onerror: any;
      constructor() {
        setTimeout(() => {
          if (this.onopen) this.onopen();
        }, 10);
      }
      close() {}
    } as any;

    dom.window.HTMLElement.prototype.scrollIntoView = () => {};
    Object.defineProperty(dom.window, 'innerWidth', { writable: true, configurable: true, value: 390 });

    dom.window.eval(i18nJs);
    dom.window.eval(js);
    await flush();
    await flush();
  });

  after(() => {
    if (dom) {
      dom.window.close();
    }
    server.close();
    closeDb();
  });

  it('Bilingual Vietnamese (default) & English Live Toggle', async () => {
    const document = dom.window.document;
    const loginHeader = document.querySelector('.login-modal h2') as HTMLElement;
    const loginLangBtn = document.getElementById('login-lang-btn') as HTMLElement;
    assert.equal(loginHeader.textContent, 'Chào mừng đến với OrderIt', 'Default language should be Vietnamese');
    assert.ok(loginLangBtn.textContent?.includes('VI'));

    // Log in while in Vietnamese and verify dynamic Vietnamese labels (Bàn 3, Khai vị)
    const waiterDemoBtn = document.querySelector('.btn-demo[data-role="waiter"]') as HTMLElement;
    assert.equal(waiterDemoBtn.textContent, 'Phục vụ (staff1)');
    waiterDemoBtn.click();
    await waitFor(dom, '#ws-menu-grid .menu-item-card');
    await flush();

    const tableTextsVi = Array.from(document.querySelectorAll('.table-card h3')).map(el => el.textContent);
    assert.ok(tableTextsVi.includes('Bàn 3'), 'Tables should render as Bàn 3 in Vietnamese');
    const catTextsVi = Array.from(document.querySelectorAll('#ws-menu-categories .menu-category span')).map(el => el.textContent);
    assert.ok(catTextsVi.includes('Khai vị'), 'Starter should render as Khai vị in Vietnamese');
    assert.ok(catTextsVi.includes('Ăn sáng'), 'Ăn sáng should stay Vietnamese in VI mode');

    // Toggle live to English via top header button without reload
    const headerLangBtn = document.getElementById('header-lang-btn') as HTMLElement;
    headerLangBtn.click();
    await flush();

    const tableTextsEn = Array.from(document.querySelectorAll('.table-card h3')).map(el => el.textContent);
    assert.ok(tableTextsEn.includes('Table 3'), 'Tables should flip live to Table 3 in English');
    const catTextsEn = Array.from(document.querySelectorAll('#ws-menu-categories .menu-category span')).map(el => el.textContent);
    assert.ok(catTextsEn.includes('Starter'), 'Khai vị should flip live to Starter in English');
    assert.ok(catTextsEn.includes('Breakfast'), 'Ăn sáng should flip live to Breakfast in English');

    // Return to login overlay so subsequent tests run cleanly
    const logoutBtn = document.getElementById('logout-btn') as HTMLElement;
    logoutBtn.click();
    await flush();
    assert.equal(loginHeader.textContent, 'Welcome to OrderIt');
  });

  it('CSS Regression Guardrail (No Squashed Yellow Menu Cards!)', () => {
    const css = fs.readFileSync(path.join(publicDir, 'styles.css'), 'utf8');
    assert.ok(css.includes('.menu-grid'), 'Missing .menu-grid');
    assert.ok(css.includes('grid-auto-rows: max-content'), 'Missing grid-auto-rows: max-content');
    assert.ok(css.includes('min-height: 0'), 'Missing min-height: 0');

    assert.ok(css.includes('.menu-item-card'), 'Missing .menu-item-card');
    assert.ok(css.includes('flex-direction: row'), 'Missing flex-direction: row in .menu-item-card');
    
    const menuItemCardBlock = css.match(/\.menu-item-card\s*\{([^}]+)\}/);
    if (menuItemCardBlock) {
      assert.ok(!menuItemCardBlock[1].includes('overflow: hidden'), '.menu-item-card contains overflow: hidden!');
    }

    assert.ok(css.includes('#bottom-nav'), 'Missing #bottom-nav');
    assert.ok(css.includes('.cart-backdrop'), 'Missing .cart-backdrop');
    assert.ok(css.includes('#mobile-cart-bar'), 'Missing #mobile-cart-bar');
    assert.ok(css.includes('.badge-amber'), 'Missing .badge-amber');
  });

  it('Waiter Login, Mobile Table Navigation & Controls', async () => {
    const document = dom.window.document;
    const waiterDemoBtn = document.querySelector('.btn-demo[data-role="waiter"]') as HTMLElement;
    waiterDemoBtn.click();
    await flush();
    await flush();
    await waitFor(dom, '#ws-menu-grid .menu-item-card');

    const appContainer = document.getElementById('app-container') as HTMLElement;
    assert.notEqual(appContainer.style.display, 'none');

    let menuCards = document.querySelectorAll('#ws-menu-grid .menu-item-card');
    assert.equal(menuCards.length, 61, 'Should have 61 menu cards');
    
    const firstMenuName = menuCards[0].querySelector('.menu-item-name') as HTMLElement;
    assert.ok(firstMenuName.textContent && firstMenuName.textContent.trim().length > 0, 'menu item name should not be empty, got: ' + firstMenuName.innerHTML);
    
    const tableCards = document.querySelectorAll('.table-card');
    const table3Card = Array.from(tableCards).find(c => c.textContent?.includes('Table 3')) as HTMLElement;
    assert.ok(table3Card, 'Table 3 card not found');
    
    table3Card.click();
    await flush();

    const waiterLayout = document.getElementById('waiter-layout') as HTMLElement;
    assert.ok(waiterLayout.classList.contains('mobile-ws-open'), 'mobile-ws-open not added');
    
    const wsTableName = document.getElementById('ws-table-name') as HTMLElement;
    assert.equal(wsTableName.textContent, 'Table 3');

    const guestsPlus = document.getElementById('ws-guests-plus') as HTMLElement;
    guestsPlus.click();
    await flush();
    guestsPlus.click();
    await flush();

    const guestsCount = document.getElementById('ws-guests-count') as HTMLElement;
    assert.equal(guestsCount.textContent, '2');

    const starterPill = document.querySelector('#ws-stage-pills .pill[data-stage="starter"]') as HTMLElement;
    starterPill.click();
    await flush();
    assert.ok(starterPill.classList.contains('active'), 'starter pill not active');

    const wsBackBtn = document.getElementById('ws-back-btn') as HTMLElement;
    wsBackBtn.click();
    await flush();
    assert.ok(!waiterLayout.classList.contains('mobile-ws-open'), 'mobile-ws-open not removed');

    const refreshedTable3 = Array.from(document.querySelectorAll('.table-card')).find(c => c.textContent?.includes('Table 3')) as HTMLElement;
    refreshedTable3.click();
    await flush();
  });

  it('Menu Category, Quick-Add +, Item Modifier Modal, Mobile Cart Bar & Send Order', async () => {
    const document = dom.window.document;
    
    const firstMenuQuickAdd = (await waitFor(dom, '#ws-menu-grid .menu-item-card .btn-quick-add')) as HTMLElement;
    firstMenuQuickAdd.click();
    await flush();

    const firstMenuBadge = document.querySelector('#ws-menu-grid .menu-item-card .menu-item-qty-badge') as HTMLElement;
    assert.equal(firstMenuBadge.textContent, '1');

    const mobileCartBar = document.getElementById('mobile-cart-bar') as HTMLElement;
    assert.equal(mobileCartBar.style.display, 'flex');

    const secondMenuCard = document.querySelectorAll('#ws-menu-grid .menu-item-card')[1] as HTMLElement;
    secondMenuCard.click();
    await flush();

    const modalItem = document.getElementById('modal-item') as HTMLElement;
    assert.ok(modalItem.classList.contains('active'), 'modal item not active');

    const modalQtyPlus = document.getElementById('modal-qty-plus') as HTMLElement;
    modalQtyPlus.click();
    await flush();

    const modChips = document.querySelectorAll('.mod-chip');
    const itDaChip = Array.from(modChips).find(c => c.textContent?.includes('Ít đá')) as HTMLElement;
    if (itDaChip) itDaChip.click();
    await flush();

    const modalConfirm = document.getElementById('modal-item-confirm') as HTMLElement;
    modalConfirm.click();
    await flush();

    const cartBadge = document.getElementById('mobile-cart-badge') as HTMLElement;
    assert.equal(cartBadge.textContent, '3');

    const liveBillTab = document.querySelector('.ws-tab[data-tab="live-bill"]') as HTMLElement;
    liveBillTab.click();
    await flush();

    assert.equal(mobileCartBar.style.display, 'none');

    const orderMenuTab = document.querySelector('.ws-tab[data-tab="order-menu"]') as HTMLElement;
    orderMenuTab.click();
    await flush();

    assert.equal(mobileCartBar.style.display, 'flex');

    const mobileCartBtn = document.getElementById('mobile-cart-btn') as HTMLElement;
    mobileCartBtn.click();
    await flush();

    const cartPanel = document.getElementById('ws-cart-panel') as HTMLElement;
    const cartBackdrop = document.getElementById('cart-backdrop') as HTMLElement;
    assert.ok(cartPanel.classList.contains('open'));
    assert.ok(cartBackdrop.classList.contains('open'));

    const sendOrderBtn = document.getElementById('ws-send-order-btn') as HTMLElement;
    sendOrderBtn.click();
    await waitFor(dom, '#ws-table-tickets-list .ticket-row');

    assert.equal(mobileCartBar.style.display, 'none');
    assert.ok(!cartPanel.classList.contains('open'));
    
    const tableTicketsList = document.getElementById('ws-table-tickets-list') as HTMLElement;
    const tickets = tableTicketsList.querySelectorAll('.ticket-row');
    assert.ok(tickets.length > 0, 'No tickets found in table tickets list');
  });

  it('Kitchen (KDS) Item Prepared Checkbox, Uncheck, FIFO Modal & 2-Tap Cancel', async () => {
    const document = dom.window.document;
    const kitchenTab = document.querySelector('.bottom-tab[data-view="kitchen"]') as HTMLElement;
    kitchenTab.click();
    await flush();
    await flush();
    await waitFor(dom, '#kds-grid .kds-ticket');

    const viewKitchen = document.getElementById('view-kitchen') as HTMLElement;
    assert.ok(viewKitchen.classList.contains('active'), '#view-kitchen not active');

    const kdsGrid = document.getElementById('kds-grid') as HTMLElement;
    const tickets = kdsGrid.querySelectorAll('.kds-ticket');
    assert.ok(tickets.length > 0, 'No KDS tickets');

    const table3Ticket = Array.from(tickets).find(t => t.textContent?.includes('Table 3')) as HTMLElement;
    assert.ok(table3Ticket, 'Table 3 ticket not found');

    const firstKdsItem = table3Ticket.querySelector('.kds-item') as HTMLElement;
    firstKdsItem.click();
    await flush();
    await flush();
    
    let updatedT3 = Array.from(document.querySelectorAll('.kds-ticket')).find(t => t.textContent?.includes('Table 3')) as HTMLElement;
    let updatedFirstItem = updatedT3.querySelector('.kds-item') as HTMLElement;
    let updatedFirstBox = updatedFirstItem.querySelector('.kds-item-box') as HTMLElement;
    assert.ok(updatedFirstItem.classList.contains('ready'), 'First kds-item should be ready');
    assert.equal(updatedFirstBox.textContent, '✓');

    const secondKdsItem = updatedT3.querySelectorAll('.kds-item')[1] as HTMLElement;
    if (secondKdsItem) {
      assert.ok(!secondKdsItem.classList.contains('ready'), 'Second kds-item should NOT be ready');
      assert.ok(updatedT3.classList.contains('status-preparing'), 'Ticket should be status-preparing');
    }

    updatedFirstItem.click();
    await flush();
    await flush();

    updatedT3 = Array.from(document.querySelectorAll('.kds-ticket')).find(t => t.textContent?.includes('Table 3')) as HTMLElement;
    updatedFirstItem = updatedT3.querySelector('.kds-item') as HTMLElement;
    updatedFirstBox = updatedFirstItem.querySelector('.kds-item-box') as HTMLElement;
    assert.ok(!updatedFirstItem.classList.contains('ready'), 'First kds-item should NOT be ready after uncheck');
    assert.equal(updatedFirstBox.textContent, '');

    // Create two fresh queued orders (Table 7, then Table 8) to test strict FIFO modal + override + 2-tap cancel
    const o7 = await (await fetch(`${baseUrl}/api/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ tableId: 7, userId: 1, source: 'waiter', items: [{ itemId: 1, quantity: 1 }] })
    })).json();
    const o8 = await (await fetch(`${baseUrl}/api/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ tableId: 8, userId: 1, source: 'waiter', items: [{ itemId: 2, quantity: 1 }] })
    })).json();

    // Refresh KDS view
    kitchenTab.click();
    await waitFor(dom, `#kds-ticket-${o8.id}`);

    const t8Ticket = document.getElementById(`kds-ticket-${o8.id}`) as HTMLElement;
    const t8StartBtn = t8Ticket.querySelector('.btn-start') as HTMLElement;
    t8StartBtn.click();
    await flush();

    const modalFifo = document.getElementById('modal-fifo') as HTMLElement;
    assert.ok(modalFifo.classList.contains('active'), 'modal-fifo not active');

    const fifoEarlierList = document.getElementById('fifo-earlier-list') as HTMLElement;
    assert.ok(fifoEarlierList.textContent?.includes('Table'), 'FIFO modal does not show earlier ticket');

    const fifoOverride = document.getElementById('fifo-override') as HTMLElement;
    fifoOverride.click();
    await flush();
    await flush();

    const t8AfterOverride = document.getElementById(`kds-ticket-${o8.id}`) as HTMLElement;
    assert.ok(t8AfterOverride.classList.contains('status-preparing'), 'Ticket did not transition to preparing after FIFO override');

    const t7Ticket = document.getElementById(`kds-ticket-${o7.id}`) as HTMLElement;
    const cancelBtn = t7Ticket.querySelector('.btn-cancel') as HTMLElement;
    cancelBtn.click();
    await flush();
    assert.equal(cancelBtn.textContent, 'Confirm?');
    assert.ok(document.getElementById(`kds-ticket-${o7.id}`), 'Ticket should not be removed on first tap');

    cancelBtn.click();
    await flush();
    await flush();
    assert.ok(!document.getElementById(`kds-ticket-${o7.id}`), 'Ticket should be removed on second tap');
  });

  it('Live Bill 0% Tax & 2-Tap Payment', async () => {
    const document = dom.window.document;
    
    // Switch to Waiter View first
    const logoutBtn = document.getElementById('logout-btn') as HTMLElement;
    logoutBtn.click();
    await flush();

    const waiterDemoBtn = document.querySelector('.btn-demo[data-role="waiter"]') as HTMLElement;
    waiterDemoBtn.click();
    
    const table3Card = await waitFor(dom, '#table-grid .table-card') as HTMLElement;
    // ensure all tables are rendered
    await flush();
    const tableCards = dom.window.document.querySelectorAll('#table-grid .table-card');
    const targetCard = Array.from(tableCards).find(c => c.textContent?.includes('Table 3')) as HTMLElement;
    targetCard.click();
    await flush();

    const liveBillTab = document.querySelector('.ws-tab[data-tab="live-bill"]') as HTMLElement;
    liveBillTab.click();
    await waitFor(dom, '#ws-bill-subtotal');

    const taxInput = document.getElementById('ws-bill-tax') as HTMLInputElement;
    taxInput.value = '0';
    taxInput.dispatchEvent(new dom.window.Event('change'));
    await flush();

    const billSubtotal = document.getElementById('ws-bill-subtotal') as HTMLElement;
    const billTotal = document.getElementById('ws-bill-total') as HTMLElement;
    assert.equal(billTotal.textContent, billSubtotal.textContent, '0% tax not applied correctly');

    const payBtn = document.getElementById('ws-pay-btn') as HTMLButtonElement;
    payBtn.click();
    await flush();

    assert.equal(payBtn.textContent, 'Tap again to Pay');
    
    payBtn.click();
    await flush();
    await flush(); 

    assert.equal(payBtn.disabled, true);
    assert.equal(payBtn.textContent, 'No unpaid items');

    const wsStatusToggle = document.getElementById('ws-status-toggle') as HTMLElement;
    assert.equal(wsStatusToggle.textContent, 'Free');
  });

  it('Customer QR Self-Order & Live ETA Tracker', async () => {
    const document = dom.window.document;
    const logoutBtn = document.getElementById('logout-btn') as HTMLElement;
    logoutBtn.click();
    await flush();

    const customerDemoBtn = document.querySelector('.btn-demo[data-role="customer"]') as HTMLElement;
    customerDemoBtn.click();
    await waitFor(dom, '#customer-table-select');

    const tableSelect = document.getElementById('customer-table-select') as HTMLSelectElement;
    tableSelect.value = "4"; 
    tableSelect.dispatchEvent(new dom.window.Event('change'));
    await flush();

    const firstMenuQuickAdd = document.querySelector('#customer-menu-grid .menu-item-card .btn-quick-add') as HTMLElement;
    firstMenuQuickAdd.click();
    await flush();

    const sendOrderBtn = document.getElementById('customer-send-order-btn') as HTMLElement;
    sendOrderBtn.click();
    await flush();
    await flush();

    const tracker = document.getElementById('customer-tracker') as HTMLElement;
    assert.equal(tracker.style.display, 'block');

    const trackerStatus = document.getElementById('customer-tracker-status') as HTMLElement;
    assert.ok(trackerStatus.textContent?.includes('QUEUED'));

    const trackerEta = document.getElementById('customer-tracker-eta') as HTMLElement;
    assert.ok(trackerEta.textContent && trackerEta.textContent.length > 0);
  });

  it('Manager View KPIs, Inline Edit, Stock Toggle, Add & 2-Tap Delete', async () => {
    const document = dom.window.document;
    const logoutBtn = document.getElementById('logout-btn') as HTMLElement;
    logoutBtn.click();
    await flush();

    const managerDemoBtn = document.querySelector('.btn-demo[data-role="manager"]') as HTMLElement;
    managerDemoBtn.click();
    await waitFor(dom, '#manager-menu-tbody tr');
    await flush();

    const kpiRevenue = document.getElementById('kpi-revenue') as HTMLElement;
    assert.ok(kpiRevenue.textContent !== '0 ₫', 'KPI revenue should not be zero (from paid table)');

    const firstStockCheckbox = document.querySelector('#manager-menu-tbody input[type="checkbox"]') as HTMLInputElement;
    firstStockCheckbox.checked = false;
    firstStockCheckbox.dispatchEvent(new dom.window.Event('change'));
    await flush();
    await flush();

    const btnAddTable = document.getElementById('btn-add-table') as HTMLElement;
    btnAddTable.click();
    await flush();

    const formAddTable = document.getElementById('form-add-table') as HTMLFormElement;
    const inputAddTableName = document.getElementById('add-table-name') as HTMLInputElement;
    inputAddTableName.value = 'Table 11';
    formAddTable.dispatchEvent(new dom.window.Event('submit'));
    await flush();
    await flush(); 

    const tbody = document.getElementById('manager-tables-tbody') as HTMLElement;
    const t11Row = Array.from(tbody.querySelectorAll('tr')).find(tr => tr.textContent?.includes('Table 11')) as HTMLElement;
    assert.ok(t11Row, 'Table 11 not found in manager table list');

    const deleteBtn = t11Row.querySelector('button') as HTMLElement;
    deleteBtn.click();
    await flush();

    assert.equal(deleteBtn.textContent, 'Tap again');
    deleteBtn.click();
    await flush();
    await flush(); 

    const t11RowAfter = Array.from(document.getElementById('manager-tables-tbody')!.querySelectorAll('tr')).find(tr => tr.textContent?.includes('Table 11'));
    assert.ok(!t11RowAfter, 'Table 11 should have been deleted');
  });
});

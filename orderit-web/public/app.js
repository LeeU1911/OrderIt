// app.js

const API_BASE = '/api';

// State
let state = {
  currentUser: null,
  tables: [],
  categories: [],
  menuItems: [],
  kdsQueue: [],
  activeView: 'waiter',
  selectedTableId: null,
  waiterCart: [],
  customerCart: [],
  customerTableId: null,
  customerOrder: null,
  analytics: null
};

// Elements
const els = {
  toast: document.getElementById('toast-container'),
  loginOverlay: document.getElementById('login-overlay'),
  appContainer: document.getElementById('app-container'),
  loginForm: document.getElementById('login-form'),
  loginUsername: document.getElementById('login-username'),
  loginPassword: document.getElementById('login-password'),
  demoBtns: document.querySelectorAll('.btn-demo'),
  currentUserDisplay: document.getElementById('current-user-display'),
  logoutBtn: document.getElementById('logout-btn'),
  navTabs: document.querySelectorAll('.nav-tab'),
  bottomTabs: document.querySelectorAll('.bottom-tab'),
  views: document.querySelectorAll('.view'),
  sseBadge: document.getElementById('sse-badge'),
  audioBtn: document.getElementById('toggle-audio-btn'),
  kdsBadge: document.getElementById('kds-badge'),
  kdsBadgeMobile: document.getElementById('kds-badge-mobile'),
  cartBackdrop: document.getElementById('cart-backdrop'),
  
  // Waiter
  waiterLayout: document.getElementById('waiter-layout'),
  tableGrid: document.getElementById('table-grid'),
  tableFilters: document.getElementById('table-filters'),
  tableSearch: document.getElementById('table-search'),
  emptyWs: document.getElementById('empty-workspace'),
  activeWs: document.getElementById('active-workspace'),
  wsBackBtn: document.getElementById('ws-back-btn'),
  wsTableName: document.getElementById('ws-table-name'),
  wsStatusToggle: document.getElementById('ws-status-toggle'),
  wsGuestsMinus: document.getElementById('ws-guests-minus'),
  wsGuestsPlus: document.getElementById('ws-guests-plus'),
  wsGuestsCount: document.getElementById('ws-guests-count'),
  wsReservedToggle: document.getElementById('ws-reserved-toggle'),
  wsStagePills: document.querySelectorAll('#ws-stage-pills .pill'),
  wsTabs: document.querySelectorAll('.ws-tab'),
  wsTabContents: document.querySelectorAll('.ws-tab-content'),
  wsMenuCategories: document.getElementById('ws-menu-categories'),
  wsMenuGrid: document.getElementById('ws-menu-grid'),
  menuSearch: document.getElementById('menu-search'),
  wsCartItems: document.getElementById('ws-cart-items'),
  wsCartPrep: document.getElementById('ws-cart-prep'),
  wsCartNote: document.getElementById('ws-cart-note'),
  wsSendOrderBtn: document.getElementById('ws-send-order-btn'),
  wsBillItems: document.getElementById('ws-bill-items'),
  wsBillSubtotal: document.getElementById('ws-bill-subtotal'),
  wsBillTotal: document.getElementById('ws-bill-total'),
  wsBillTax: document.getElementById('ws-bill-tax'),
  wsBillDiscount: document.getElementById('ws-bill-discount'),
  wsBillReqToggle: document.getElementById('ws-bill-req-toggle'),
  wsPayBtn: document.getElementById('ws-pay-btn'),
  wsTableTickets: document.getElementById('ws-table-tickets-list'),

  // Kitchen
  kdsGrid: document.getElementById('kds-grid'),
  kdsStationFilter: document.getElementById('kds-station-filter'),

  // Customer
  customerTableSelect: document.getElementById('customer-table-select'),
  customerMain: document.getElementById('customer-main'),
  customerTracker: document.getElementById('customer-tracker'),
  customerTrackerStatus: document.getElementById('customer-tracker-status'),
  customerTrackerEta: document.getElementById('customer-tracker-eta'),
  customerCategories: document.getElementById('customer-menu-categories'),
  customerMenuSearch: document.getElementById('customer-menu-search'),
  customerMenuGrid: document.getElementById('customer-menu-grid'),
  customerCartItems: document.getElementById('customer-cart-items'),
  customerSendOrderBtn: document.getElementById('customer-send-order-btn'),

  // Manager
  managerRevenue: document.getElementById('kpi-revenue'),
  managerPaidOrders: document.getElementById('kpi-paid-orders'),
  managerActiveOrders: document.getElementById('kpi-active-orders'),
  managerAvgPrep: document.getElementById('kpi-avg-prep'),
  managerMenuTbody: document.getElementById('manager-menu-tbody'),
  managerTablesTbody: document.getElementById('manager-tables-tbody'),
  managerRankingList: document.getElementById('manager-ranking-list'),
  btnAddMenuItem: document.getElementById('btn-add-menu-item'),
  btnAddTable: document.getElementById('btn-add-table'),

  // Modals
  modalItem: document.getElementById('modal-item'),
  modalItemName: document.getElementById('modal-item-name'),
  modalQtyVal: document.getElementById('modal-qty-val'),
  modalQtyMinus: document.getElementById('modal-qty-minus'),
  modalQtyPlus: document.getElementById('modal-qty-plus'),
  modalItemNote: document.getElementById('modal-item-note'),
  modalItemCancel: document.getElementById('modal-item-cancel'),
  modalItemConfirm: document.getElementById('modal-item-confirm'),
  modalModifiers: document.getElementById('modal-modifiers'),
  
  modalFifo: document.getElementById('modal-fifo'),
  fifoEarlierList: document.getElementById('fifo-earlier-list'),
  fifoCancel: document.getElementById('fifo-cancel'),
  fifoOverride: document.getElementById('fifo-override'),

  modalMenuForm: document.getElementById('modal-menu-form'),
  modalMenuCancel: document.getElementById('modal-menu-cancel'),
  formAddMenu: document.getElementById('form-add-menu'),
  
  modalTableForm: document.getElementById('modal-table-form'),
  modalTableCancel: document.getElementById('modal-table-cancel'),
  formAddTable: document.getElementById('form-add-table')
};

// Utils
const formatMoney = (val) => new Intl.NumberFormat('vi-VN').format(val) + ' ₫';

function formatStageBadge(stage) {
  const map = {
    available: 'Avail',
    starter: 'Start',
    mains: 'Main',
    dessert: 'Dess',
    bill: 'Bill'
  };
  return window.t ? window.t(map[stage] || stage) : stage;
}

function formatTableName(name) {
  if (window.currentLang === 'vi' && name.toLowerCase().startsWith('table ')) {
    return `Bàn ${name.substring(6)}`;
  }
  if (window.currentLang === 'en' && name.toLowerCase().startsWith('bàn ')) {
    return `Table ${name.substring(4)}`;
  }
  return name;
}

function formatCategoryName(name) {
  return window.t ? window.t(name) : name;
}

function formatOrderStatus(status) {
  const map = {
    queued: 'Queued',
    preparing: 'Preparing',
    ready: 'Ready',
    served: 'Served',
    paid: 'Paid',
    cancelled: 'Cancelled'
  };
  return window.t ? window.t(map[status] || status) : status;
}

const escapeHTML = (str) => {
  if (!str) return '';
  const div = document.createElement('div');
  div.textContent = str;
  return div.innerHTML;
};
function showToast(msg, isError = false) {
  const toastEl = document.createElement('div');
  toastEl.className = 'toast show';
  toastEl.style.backgroundColor = isError ? 'var(--color-red)' : 'var(--color-charcoal)';
  toastEl.textContent = window.t ? window.t(msg) : msg;
  els.toast.appendChild(toastEl);
  setTimeout(() => { toastEl.classList.remove('show'); setTimeout(() => toastEl.remove(), 300); }, 3000);
}

function twoTapConfirm(btn, normalText, confirmText, className, onConfirm) {
  if (btn.dataset.confirming === 'true') {
    delete btn.dataset.confirming;
    btn.innerHTML = normalText;
    btn.classList.remove(className);
    onConfirm();
  } else {
    btn.dataset.confirming = 'true';
    btn.innerHTML = confirmText;
    btn.classList.add(className);
    setTimeout(() => {
      if (btn.dataset.confirming === 'true') {
        delete btn.dataset.confirming;
        btn.innerHTML = normalText;
        btn.classList.remove(className);
      }
    }, 3000);
  }
}

// Audio
let audioCtx = null;
let audioEnabled = false;
els.audioBtn.addEventListener('click', () => {
  audioEnabled = !audioEnabled;
  els.audioBtn.textContent = audioEnabled ? '🔔' : '🔕';
  if (audioEnabled && !audioCtx) {
    audioCtx = new (window.AudioContext || window.webkitAudioContext)();
  }
  if (audioEnabled) playChime(); 
});

function playChime() {
  if (!audioEnabled || !audioCtx) return;
  const osc = audioCtx.createOscillator();
  const gain = audioCtx.createGain();
  osc.type = 'sine';
  osc.frequency.setValueAtTime(880, audioCtx.currentTime);
  osc.frequency.exponentialRampToValueAtTime(440, audioCtx.currentTime + 0.1);
  gain.gain.setValueAtTime(0.5, audioCtx.currentTime);
  gain.gain.exponentialRampToValueAtTime(0.01, audioCtx.currentTime + 0.5);
  osc.connect(gain);
  gain.connect(audioCtx.destination);
  osc.start();
  osc.stop(audioCtx.currentTime + 0.5);
}

// SSE
let eventSource = null;
function connectSSE() {
  if (eventSource) eventSource.close();
  eventSource = new EventSource(`${API_BASE}/events`);
  
  eventSource.onopen = () => {
    els.sseBadge.classList.remove('offline');
    els.sseBadge.textContent = '● Live';
  };
  eventSource.onerror = () => {
    els.sseBadge.classList.add('offline');
    els.sseBadge.textContent = 'Reconnecting...';
  };
  eventSource.onmessage = (e) => {
    const data = JSON.parse(e.data);
    handleServerEvent(data);
  };
}

function handleServerEvent(data) {
  const t = data.type;
  if (t === 'table.updated' || t === 'table.deleted') fetchTables();
  if (t === 'menu.updated') fetchMenu();
  if (t === 'order.created' || t === 'order.updated' || t === 'order.paid') {
    if (t === 'order.created' && ['kitchen', 'manager'].includes(state.currentUser?.role)) {
      playChime();
    }
    fetchKdsQueue();
    if (state.selectedTableId) {
      fetchTableTickets(state.selectedTableId);
      fetchTableBill(state.selectedTableId);
    }
    if (state.customerTableId) fetchCustomerOrder();
    if (state.activeView === 'manager') fetchAnalytics();
    
    if (t === 'order.paid') {
      fetchTables();
    }
  }
}

// API
async function api(path, method = 'GET', body = null) {
  const opts = { method, headers: {} };
  if (body) {
    opts.headers['Content-Type'] = 'application/json';
    opts.body = JSON.stringify(body);
  }
  const res = await fetch(`${API_BASE}${path}`, opts);
  if (!res.ok) {
    const err = await res.json();
    throw err;
  }
  return res.json();
}

// Auth
els.loginForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  const u = els.loginUsername.value;
  const p = els.loginPassword.value;
  try {
    const res = await api('/auth/login', 'POST', { username: u, password: p });
    login(res.user);
  } catch (err) {
    showToast(err.error || 'Login failed', true);
  }
});
els.demoBtns.forEach(b => {
  b.addEventListener('click', async () => {
    if (b.dataset.role === 'customer') {
      login({ id: 1, role: 'customer', username: 'Guest', display_name: 'Guest' });
    } else {
      try {
        const res = await api('/auth/login', 'POST', { username: b.dataset.user, password: 'password' });
        login(res.user);
      } catch (err) {
        showToast(err.error || 'Login failed', true);
      }
    }
  });
});
  els.logoutBtn.addEventListener('click', () => {
    state.currentUser = null;
    if (timerInterval) { clearInterval(timerInterval); timerInterval = null; }
    els.appContainer.style.display = 'none';
    els.loginOverlay.classList.add('active');
    if (eventSource) eventSource.close();
  });

function login(user) {
  state.currentUser = user;
  els.currentUserDisplay.textContent = user.username;
  els.loginOverlay.classList.remove('active');
  els.appContainer.style.display = 'flex';
  
  if (user.role === 'kitchen') switchView('kitchen');
  else if (user.role === 'customer') switchView('customer');
  else if (user.role === 'manager') switchView('manager');
  else switchView('waiter');

  initApp();
}

let timerInterval = null;
function initApp() {
  connectSSE();
  fetchTables();
  fetchMenu();
  fetchKdsQueue();
  if (state.currentUser?.role === 'manager') fetchAnalytics();
  
  if (timerInterval) clearInterval(timerInterval);
  timerInterval = setInterval(updateTimers, 1000);
}

// View switching
function bindTabs(tabs) {
  tabs.forEach(tab => {
    tab.addEventListener('click', () => switchView(tab.dataset.view));
  });
}
bindTabs(els.navTabs);
bindTabs(els.bottomTabs);

function switchView(viewName) {
  state.activeView = viewName;
  els.navTabs.forEach(t => t.classList.toggle('active', t.dataset.view === viewName));
  els.bottomTabs.forEach(t => t.classList.toggle('active', t.dataset.view === viewName));
  els.views.forEach(v => v.classList.toggle('active', v.id === `view-${viewName}`));
  if (viewName === 'manager') fetchAnalytics();
  if (viewName === 'kitchen') fetchKdsQueue();
  updateMobileCartBar();
}

let activeWaiterSubTab = 'order-menu';

els.wsBackBtn.addEventListener('click', () => {
  els.waiterLayout.classList.remove('mobile-ws-open');
  closeCartDrawers();
  updateMobileCartBar();
});

// Sub-tabs
els.wsTabs.forEach(tab => {
  tab.addEventListener('click', () => {
    activeWaiterSubTab = tab.dataset.tab;
    els.wsTabs.forEach(t => t.classList.remove('active'));
    els.wsTabContents.forEach(t => t.classList.remove('active'));
    tab.classList.add('active');
    document.getElementById(`tab-${tab.dataset.tab}`).classList.add('active');
    closeCartDrawers();
    updateMobileCartBar();
  });
});

// Fetch Data
async function fetchTables() {
  try {
    state.tables = await api('/tables');
    renderTables();
    renderCustomerTables();
    if (state.activeView === 'manager') renderManagerTables();
    if (state.selectedTableId) refreshSelectedTableUI(state.selectedTableId);
  } catch(e) { console.error(e); }
}

async function fetchMenu() {
  try {
    const { items, categories, topItems } = await api('/menu');
    state.menuItems = items;
    state.categories = categories;
    state.topItems = topItems || [];
    
    const categorySelect = document.getElementById('add-menu-category');
    if (categorySelect) {
      categorySelect.innerHTML = categories.map(c => `<option value="${c.id}">${escapeHTML(c.name)}</option>`).join('');
    }
    
    renderMenuCategories(els.wsMenuCategories, true);
    renderMenuCategories(els.customerCategories, false);
    renderMenuItems(els.wsMenuGrid, true);
    renderMenuItems(els.customerMenuGrid, false);
    if (state.activeView === 'manager') renderManagerMenu();
  } catch(e) { console.error(e); }
}

async function fetchKdsQueue() {
  try {
    const station = els.kdsStationFilter.value;
    state.kdsQueue = await api(station ? `/orders/kitchen?station=${station}` : '/orders/kitchen');
    els.kdsBadge.textContent = state.kdsQueue.length;
    if (els.kdsBadgeMobile) els.kdsBadgeMobile.textContent = state.kdsQueue.length;
    renderKds();
  } catch(e) { console.error(e); }
}

async function fetchAnalytics() {
  try {
    state.analytics = await api('/analytics');
    renderManager();
  } catch(e) { console.error(e); }
}

// --- WAITER / TABLES --- //
let activeTableFilter = 'all';
els.tableFilters.addEventListener('click', (e) => {
  if(e.target.classList.contains('chip')) {
    document.querySelectorAll('#table-filters .chip').forEach(c => c.classList.remove('active'));
    e.target.classList.add('active');
    activeTableFilter = e.target.dataset.filter;
    renderTables();
  }
});
els.tableSearch.addEventListener('input', renderTables);

function renderTables() {
  els.tableGrid.innerHTML = '';
  const q = els.tableSearch.value.toLowerCase();
  
  let filtered = state.tables.filter(t => t.name.toLowerCase().includes(q));
  if (activeTableFilter === 'available') filtered = filtered.filter(t => t.status === 'available');
  if (activeTableFilter === 'occupied') filtered = filtered.filter(t => t.status === 'occupied');
  if (activeTableFilter === 'bill') filtered = filtered.filter(t => t.status === 'bill_requested');
  
  filtered.forEach(t => {
    const card = document.createElement('div');
    card.className = `table-card ${t.id === state.selectedTableId ? 'active' : ''}`;
    card.onclick = () => selectTable(t.id);
    
    let dotClass = '';
    if (t.status === 'occupied') dotClass = 'occupied';
    if (t.status === 'bill_requested') dotClass = 'bill';
    
    card.innerHTML = `
      <div class="dot ${dotClass}"></div>
      <h3>${escapeHTML(formatTableName(t.name))}</h3>
      <div class="info">${t.guest_count} <span style="font-size: 14px">👤</span></div>
      <div class="badge badge-${t.stage}">${formatStageBadge(t.stage)}</div>
    `;
    els.tableGrid.appendChild(card);
  });
}

function selectTable(id) {
  state.selectedTableId = id;
  state.waiterCart = [];
  els.wsCartNote.value = '';
  renderCart(els.wsCartItems, els.wsCartPrep, true);
  els.waiterLayout.classList.add('mobile-ws-open');
  refreshSelectedTableUI(id);
  renderMenuItems(els.wsMenuGrid, true); 
}

function refreshSelectedTableUI(id) {
  renderTables();
  const table = state.tables.find(t => t.id === id);
  if (!table) return;
  
  els.emptyWs.style.display = 'none';
  els.activeWs.style.display = 'flex';
  
  els.wsTableName.textContent = formatTableName(table.name);
  els.wsStatusToggle.textContent = table.status === 'available' ? 'Free' : (table.status === 'bill_requested' ? 'Bill Req' : 'Occupied');
  els.wsStatusToggle.className = `btn-toggle ${table.status === 'available' ? 'btn-secondary' : 'btn-amber'}`;
  els.wsGuestsCount.textContent = table.guest_count;
  els.wsReservedToggle.checked = !!table.reserved;
  
  els.wsStagePills.forEach(p => {
    p.classList.toggle('active', p.dataset.stage === table.stage);
  });
  
  fetchTableBill(id);
  fetchTableTickets(id);
}

// Table controls wrapper
async function withTableControl(btn, action) {
  const old = btn.disabled;
  btn.disabled = true;
  try {
    await action();
    await fetchTables();
  } catch(e) {
    showToast(e.error || 'Failed', true);
  } finally {
    btn.disabled = old;
  }
}

els.wsStatusToggle.onclick = () => withTableControl(els.wsStatusToggle, async () => {
  const table = state.tables.find(t => t.id === state.selectedTableId);
  const nextStatus = table.status === 'available' ? 'occupied' : 'available';
  await api(`/tables/${state.selectedTableId}`, 'PATCH', { status: nextStatus, stage: nextStatus === 'available' ? 'available' : 'starter' });
});
els.wsGuestsMinus.onclick = () => withTableControl(els.wsGuestsMinus, async () => {
  const table = state.tables.find(t => t.id === state.selectedTableId);
  if (table.guest_count > 0) await api(`/tables/${state.selectedTableId}`, 'PATCH', { guest_count: table.guest_count - 1 });
});
els.wsGuestsPlus.onclick = () => withTableControl(els.wsGuestsPlus, async () => {
  const table = state.tables.find(t => t.id === state.selectedTableId);
  await api(`/tables/${state.selectedTableId}`, 'PATCH', { guest_count: table.guest_count + 1 });
});
els.wsReservedToggle.onchange = (e) => withTableControl(els.wsReservedToggle, async () => {
  await api(`/tables/${state.selectedTableId}`, 'PATCH', { reserved: e.target.checked ? 1 : 0 });
});
els.wsStagePills.forEach(p => {
  p.onclick = () => withTableControl(p, async () => {
    await api(`/tables/${state.selectedTableId}`, 'PATCH', { stage: p.dataset.stage });
  });
});

async function fetchTableBill(id) {
  try {
    const bill = await api(`/tables/${id}/bill`);
    renderBill(bill);
  } catch(e) {}
}

function renderBill(bill) {
  els.wsBillItems.innerHTML = '';
  (bill.lineItems || []).forEach(i => {
    els.wsBillItems.innerHTML += `
      <div class="bill-item-row">
        <div class="qty tabular">${i.quantity}x</div>
        <div class="name">
          ${escapeHTML(i.name)} <span style="font-size:0.85rem;color:var(--color-gray-500)">(${formatMoney(i.unit_price)})</span>
          ${i.notes ? `<div style="font-size:0.85rem;color:var(--color-red);font-style:italic;">${escapeHTML(i.notes)}</div>` : ''}
          ${i.remainingMin > 0 ? `<span class="badge badge-amber" style="font-size:0.7rem;">${i.remainingMin}m</span>` : ''}
        </div>
        <div class="price tabular">${formatMoney(i.total)}</div>
      </div>
    `;
  });
  els.wsBillSubtotal.textContent = formatMoney(bill.subtotal);
  els.wsBillTotal.textContent = formatMoney(bill.grandTotal);
  els.wsBillTax.value = bill.taxRate;
  els.wsBillDiscount.value = bill.discountRate;
  
  els.wsPayBtn.disabled = bill.lineItems.length === 0;
  if(bill.lineItems.length === 0) els.wsPayBtn.textContent = window.t ? window.t('No unpaid items') : 'No unpaid items';
  else els.wsPayBtn.textContent = window.t ? window.t('Finish & Pay') : 'Finish & Pay';
  
  const table = state.tables.find(t => t.id === state.selectedTableId);
  els.wsBillReqToggle.checked = table?.status === 'bill_requested';
}

els.wsBillTax.onchange = updateRates;
els.wsBillDiscount.onchange = updateRates;
async function updateRates() {
  const tr = els.wsBillTax.value;
  const dr = els.wsBillDiscount.value;
  const t = tr === '' ? 0 : parseFloat(tr);
  const d = dr === '' ? 0 : parseFloat(dr);
  try {
    const updated = await api(`/tables/${state.selectedTableId}/bill`, 'PATCH', { taxRate: t, discountRate: d });
    renderBill(updated);
  } catch (e) { showToast(e.error, true); }
}
els.wsBillReqToggle.onchange = (e) => withTableControl(els.wsBillReqToggle, async () => {
  await api(`/tables/${state.selectedTableId}`, 'PATCH', { status: e.target.checked ? 'bill_requested' : 'occupied' });
});

els.wsPayBtn.onclick = async () => {
  const normalLbl = window.t ? window.t('Finish & Pay') : 'Finish & Pay';
  const confirmLbl = window.t ? window.t('Tap again to Pay') : 'Tap again to Pay';
  twoTapConfirm(els.wsPayBtn, normalLbl, confirmLbl, 'btn-red', async () => {
    try {
      const tr = els.wsBillTax.value;
      const dr = els.wsBillDiscount.value;
      await api(`/tables/${state.selectedTableId}/pay`, 'POST', {
        taxRate: tr === '' ? 0 : parseFloat(tr),
        discountRate: dr === '' ? 0 : parseFloat(dr)
      });
      showToast('Bill paid successfully');
      fetchTables();
      fetchTableBill(state.selectedTableId);
    } catch(e) {
      showToast(e.error || 'Payment failed', true);
    }
  });
};

async function fetchTableTickets(id) {
  try {
    const tableOrders = await api(`/orders?tableId=${id}`);
    els.wsTableTickets.innerHTML = tableOrders.map(o => `
      <div class="ticket-row">
        <div class="ticket-row-header">
          <span>Ticket #${o.ticket_number}</span>
          <span class="status-pill ${o.status}">${o.status.toUpperCase()}</span>
        </div>
        ${o.items.map(i => `
          <div class="ticket-item">
            <span>${i.quantity}x ${escapeHTML(i.item_name)}</span>
            <span class="status-pill ${i.status}">${i.status}</span>
          </div>
        `).join('')}
      </div>
    `).join('') || '<p style="color:var(--color-gray-500);text-align:center;padding:2rem;">No active orders.</p>';
  } catch(e) {}
}

// --- MENU & CART --- //
let activeWaiterCategory = null;
let activeCustomerCategory = null;
let activeMenuSearch = '';
let activeCustomerMenuSearch = '';

els.menuSearch.addEventListener('input', (e) => {
  activeMenuSearch = e.target.value.toLowerCase();
  renderMenuItems(els.wsMenuGrid, true);
});
els.customerMenuSearch.addEventListener('input', (e) => {
  activeCustomerMenuSearch = e.target.value.toLowerCase();
  renderMenuItems(els.customerMenuGrid, false);
});

function renderMenuCategories(container, isWaiter) {
  const activeCat = isWaiter ? activeWaiterCategory : activeCustomerCategory;
  container.innerHTML = `<div class="menu-category ${activeCat === null ? 'active' : ''}" onclick="filterCategory(null, ${isWaiter})">
    <img src="assets/Menu - Favorite.jpg" onerror="this.src='assets/nsr-app_icon.png'" />
    <span>All</span>
  </div>
  <div class="menu-category ${activeCat === 'top' ? 'active' : ''}" onclick="filterCategory('top', ${isWaiter})">
    <img src="assets/Menu - Favorite.jpg" onerror="this.src='assets/nsr-app_icon.png'" />
    <span>⭐ Top</span>
  </div>`;
  state.categories.forEach(c => {
    container.innerHTML += `
      <div class="menu-category ${activeCat === c.id ? 'active' : ''}" onclick="filterCategory(${c.id}, ${isWaiter})">
        <img src="assets/${c.icon || 'nsr-app_icon.png'}" onerror="this.src='assets/nsr-app_icon.png'" />
        <span>${escapeHTML(formatCategoryName(c.name))}</span>
      </div>
    `;
  });
}
window.filterCategory = (id, isWaiter) => {
  if (isWaiter) activeWaiterCategory = id;
  else activeCustomerCategory = id;
  renderMenuCategories(isWaiter ? els.wsMenuCategories : els.customerCategories, isWaiter);
  renderMenuItems(isWaiter ? els.wsMenuGrid : els.customerMenuGrid, isWaiter);
};

function renderMenuItems(container, isWaiter) {
  container.innerHTML = '';
  const activeCat = isWaiter ? activeWaiterCategory : activeCustomerCategory;
  let filtered = state.menuItems;
  if (activeCat === 'top') {
    filtered = [...(state.topItems || [])];
  } else if (activeCat) {
    filtered = filtered.filter(i => i.category_id === activeCat);
  }
  const s = isWaiter ? activeMenuSearch : activeCustomerMenuSearch;
  if (s) filtered = filtered.filter(i => i.name.toLowerCase().includes(s));
  
  const cart = isWaiter ? state.waiterCart : state.customerCart;
  
  filtered.forEach(i => {
    const card = document.createElement('div');
    card.className = `menu-item-card ${i.available ? '' : 'disabled'}`;
    card.onclick = () => openItemModal(i, isWaiter);
    const cat = state.categories.find(c => c.id === i.category_id);
    const icon = cat?.icon || 'nsr-app_icon.png';
    const qtyInCart = cart.reduce((sum, c) => c.item.id === i.id ? sum + c.qty : sum, 0);

    card.innerHTML = `
      <img src="assets/${icon}" onerror="this.src='assets/nsr-app_icon.png'" />
      <div class="menu-item-info">
        <div class="menu-item-name">${escapeHTML(i.name)}</div>
        <div class="menu-item-meta tabular">
          🕒 ${i.average_make_time}${window.t ? window.t('m') : 'm'}
          ${i.order_count > 0 ? `<span class="fire">🔥 ${i.order_count} sold</span>` : ''}
        </div>
      </div>
      <div class="menu-item-right">
        <div class="menu-item-price tabular">${formatMoney(i.price)}</div>
        <div class="btn-quick-add-wrap">
          <button class="btn-quick-add" onclick="quickAddItem(event, ${i.id}, ${isWaiter})">+</button>
          ${qtyInCart > 0 ? `<div class="menu-item-qty-badge">${qtyInCart}</div>` : ''}
        </div>
      </div>
    `;
    container.appendChild(card);
  });
}

window.quickAddItem = (e, itemId, isWaiter) => {
  e.stopPropagation();
  const item = state.menuItems.find(i => i.id === itemId);
  if (!item || !item.available) return;
  const cart = isWaiter ? state.waiterCart : state.customerCart;
  const existing = cart.find(c => c.item.id === itemId && !c.note);
  if (existing) {
    existing.qty++;
  } else {
    cart.push({ item, qty: 1, note: '' });
  }
  showToast(`Added 1x ${item.name}`);
  
  if (isWaiter) renderCart(els.wsCartItems, els.wsCartPrep, true);
  else renderCart(els.customerCartItems, null, false);
  renderMenuItems(isWaiter ? els.wsMenuGrid : els.customerMenuGrid, isWaiter);
};

// Modal logic
let pendingItem = null;
let pendingQty = 1;
let pendingIsWaiter = true;
function openItemModal(item, isWaiter) {
  if (!item.available) return;
  pendingItem = item;
  pendingQty = 1;
  pendingIsWaiter = isWaiter;
  els.modalItemName.textContent = item.name;
  els.modalQtyVal.textContent = pendingQty;
  els.modalItemNote.value = '';
  els.modalItem.classList.add('active');
}
els.modalQtyMinus.onclick = () => { if (pendingQty > 1) els.modalQtyVal.textContent = --pendingQty; };
els.modalQtyPlus.onclick = () => { els.modalQtyVal.textContent = ++pendingQty; };
els.modalItemCancel.onclick = () => els.modalItem.classList.remove('active');
document.querySelectorAll('.mod-chip').forEach(c => {
  c.onclick = () => {
    const val = els.modalItemNote.value;
    els.modalItemNote.value = val ? val + ', ' + c.textContent : c.textContent;
  };
});
els.modalItemConfirm.onclick = () => {
  const cart = pendingIsWaiter ? state.waiterCart : state.customerCart;
  const note = els.modalItemNote.value.trim();
  const existing = cart.find(c => c.item.id === pendingItem.id && c.note === note);
  if (existing) {
    existing.qty += pendingQty;
  } else {
    cart.push({ item: pendingItem, qty: pendingQty, note });
  }
  els.modalItem.classList.remove('active');
  if (pendingIsWaiter) renderCart(els.wsCartItems, els.wsCartPrep, true);
  else renderCart(els.customerCartItems, null, false);
  renderMenuItems(pendingIsWaiter ? els.wsMenuGrid : els.customerMenuGrid, pendingIsWaiter);
};

function renderCart(container, prepContainer, isWaiter) {
  container.innerHTML = '';
  let maxPrep = 0;
  const cart = isWaiter ? state.waiterCart : state.customerCart;
  
  cart.forEach((c, idx) => {
    if (c.item.average_make_time > maxPrep) maxPrep = c.item.average_make_time;
    container.innerHTML += `
      <div class="cart-item">
        <div class="stepper" style="flex-shrink:0;">
          <button type="button" style="padding:2px 8px;font-size:1rem;" onclick="adjustCartQty(${idx}, -1, ${isWaiter})">−</button>
          <span class="tabular" style="padding:0 6px;font-size:0.9rem;">${c.qty}</span>
          <button type="button" style="padding:2px 8px;font-size:1rem;" onclick="adjustCartQty(${idx}, 1, ${isWaiter})">+</button>
        </div>
        <div class="cart-item-details">
          <div class="cart-item-name">${escapeHTML(c.item.name)}</div>
          <div class="cart-item-note">${escapeHTML(c.note)}</div>
        </div>
        <div class="cart-item-price tabular">${formatMoney(c.item.price * c.qty)}</div>
        <button type="button" class="btn-icon" style="color:var(--color-red)" onclick="removeCartItem(${idx}, ${isWaiter})">×</button>
      </div>
    `;
  });
  if (prepContainer) prepContainer.textContent = `${maxPrep}m`;
  
  if (isWaiter) {
    els.wsSendOrderBtn.disabled = cart.length === 0;
  } else {
    els.customerSendOrderBtn.disabled = cart.length === 0;
  }
  if (cart.length === 0) closeCartDrawers();
  updateMobileCartBar();
}
window.adjustCartQty = (idx, delta, isWaiter) => {
  const cart = isWaiter ? state.waiterCart : state.customerCart;
  if (!cart[idx]) return;
  cart[idx].qty += delta;
  if (cart[idx].qty <= 0) cart.splice(idx, 1);
  if (isWaiter) renderCart(els.wsCartItems, els.wsCartPrep, true);
  else renderCart(els.customerCartItems, null, false);
  renderMenuItems(isWaiter ? els.wsMenuGrid : els.customerMenuGrid, isWaiter);
};
window.removeCartItem = (idx, isWaiter) => {
  const cart = isWaiter ? state.waiterCart : state.customerCart;
  cart.splice(idx, 1);
  if (isWaiter) renderCart(els.wsCartItems, els.wsCartPrep, true);
  else renderCart(els.customerCartItems, null, false);
  renderMenuItems(isWaiter ? els.wsMenuGrid : els.customerMenuGrid, isWaiter);
};

els.wsSendOrderBtn.onclick = async () => {
  if (!state.selectedTableId || state.waiterCart.length === 0) return;
  try {
    await api('/orders', 'POST', {
      tableId: state.selectedTableId,
      userId: state.currentUser?.id || 1,
      source: 'waiter',
      notes: els.wsCartNote.value,
      items: state.waiterCart.map(c => ({ itemId: c.item.id, quantity: c.qty, notes: c.note }))
    });
    state.waiterCart = [];
    renderCart(els.wsCartItems, els.wsCartPrep, true);
    els.wsCartNote.value = '';
    closeCartDrawers();
    showToast('Order sent to kitchen!');
    await Promise.all([
      fetchTables(),
      fetchTableBill(state.selectedTableId),
      fetchTableTickets(state.selectedTableId),
      fetchKdsQueue()
    ]);
    renderMenuItems(els.wsMenuGrid, true);
  } catch(e) { showToast(e.error, true); }
};

// --- MOBILE CART BAR --- //
function closeCartDrawers() {
  document.getElementById('ws-cart-panel')?.classList.remove('open');
  document.getElementById('customer-cart-panel')?.classList.remove('open');
  els.cartBackdrop?.classList.remove('open');
}
function updateMobileCartBar() {
  const isWaiter = state.activeView === 'waiter';
  const cart = isWaiter ? state.waiterCart : state.customerCart;
  const tableId = isWaiter ? state.selectedTableId : state.customerTableId;
  const wsOpenOnMobile = els.waiterLayout.classList.contains('mobile-ws-open') || window.innerWidth >= 900;
  
  const bar = document.getElementById('mobile-cart-bar');
  if (
    !tableId ||
    cart.length === 0 ||
    ['kitchen', 'manager'].includes(state.activeView) ||
    (isWaiter && (activeWaiterSubTab !== 'order-menu' || !wsOpenOnMobile))
  ) {
    bar.style.display = 'none';
    return;
  }
  
  bar.style.display = 'flex';
  const qty = cart.reduce((sum, c) => sum + c.qty, 0);
  const total = cart.reduce((sum, c) => sum + (c.item.price * c.qty), 0);
  
  document.getElementById('mobile-cart-badge').textContent = qty;
  document.getElementById('mobile-cart-total').textContent = formatMoney(total);
}
document.getElementById('mobile-cart-btn').onclick = () => {
  const isWaiter = state.activeView === 'waiter';
  const panelId = isWaiter ? 'ws-cart-panel' : 'customer-cart-panel';
  document.getElementById(panelId).classList.add('open');
  els.cartBackdrop?.classList.add('open');
};
document.getElementById('ws-close-cart-btn').onclick = closeCartDrawers;
document.getElementById('customer-close-cart-btn').onclick = closeCartDrawers;
if (els.cartBackdrop) els.cartBackdrop.onclick = closeCartDrawers;


// --- KITCHEN (KDS) --- //
els.kdsStationFilter.onchange = fetchKdsQueue;

function renderKds() {
  els.kdsGrid.innerHTML = '';
  state.kdsQueue.forEach((o, i) => {
    const card = document.createElement('div');
    card.id = `kds-ticket-${o.id}`;
    card.className = `kds-ticket status-${o.status}`;
    card.innerHTML = `
      <div class="kds-ticket-header">
        <div class="kds-ticket-meta">
          <span>#${o.ticket_number} • Q-${o.fifoPosition || '-'}</span>
          <span class="badge ${o.source === 'customer' ? 'badge-mains' : 'badge-available'}">${window.t ? window.t(o.source === 'customer' ? 'CUSTOMER' : 'WAITER') : o.source.toUpperCase()}</span>
        </div>
        <div class="kds-ticket-title">
          <span>${escapeHTML(formatTableName(o.table_name))}</span>
          <span class="kds-timer tabular" data-status="${o.status}" data-sla="${o.slaStatus}" data-created="${o.created_at}" data-est="${(o.estimatedPrepMin || 0) * 60}"></span>
        </div>
      </div>
      <div class="kds-ticket-items">
        ${o.items.map(item => `
          <div class="kds-item ${item.status}" onclick="toggleItemStatus(${item.id}, '${item.status}')">
            <div class="kds-item-box">${item.status === 'ready' || item.status === 'served' ? '✓' : ''}</div>
            <div class="kds-item-details">
              <div class="kds-item-name tabular">${item.quantity}x ${escapeHTML(item.item_name)}</div>
              ${item.notes ? `<div class="kds-item-note">${escapeHTML(item.notes)}</div>` : ''}
            </div>
          </div>
        `).join('')}
      </div>
      <div class="kds-ticket-actions">
        ${o.status === 'queued' ? `<button type="button" class="btn-primary btn-start" onclick="updateOrderStatus(${o.id}, 'preparing')">${window.t ? window.t('Start Preparing') : 'Start Preparing'}</button><button type="button" class="btn-secondary btn-cancel" onclick="cancelTicketConfirm(this, ${o.id})">${window.t ? window.t('Cancel') : 'Cancel'}</button>` : ''}
        ${o.status === 'preparing' ? `<button type="button" class="btn-amber w-100" onclick="updateOrderStatus(${o.id}, 'ready')">${window.t ? window.t('Mark Ready') : 'Mark Ready'}</button>` : ''}
        ${o.status === 'ready' ? `<button type="button" class="btn-green w-100" onclick="updateOrderStatus(${o.id}, 'served')">${window.t ? window.t('Mark Served') : 'Mark Served'}</button>` : ''}
      </div>
    `;
    els.kdsGrid.appendChild(card);
  });
  updateTimers();
}

let pendingOverride = null;
window.updateOrderStatus = async (id, status, override = false) => {
  try {
    await api(`/orders/${id}/status`, 'PATCH', { status, opts: { forceFifoOverride: override } });
    els.modalFifo.classList.remove('active');
    await fetchKdsQueue();
  } catch(e) {
    if (e.fifoViolation) {
      pendingOverride = { id, status };
      els.fifoEarlierList.innerHTML = e.earlierOrders.map(eo => `<li data-id="${eo.id}">${window.t ? window.t('Ticket #') : 'Ticket #'} ${eo.ticket_number} (${escapeHTML(formatTableName(eo.table_name))})</li>`).join('');
      els.modalFifo.classList.add('active');
    } else {
      showToast(e.error, true);
    }
  }
};
els.fifoCancel.onclick = () => {
  els.modalFifo.classList.remove('active');
  const li = els.fifoEarlierList.querySelector('li');
  if (li && li.dataset.id) {
    const el = document.getElementById(`kds-ticket-${li.dataset.id}`);
    if (el) {
      el.scrollIntoView({ behavior: 'smooth', block: 'center' });
      el.classList.add('highlight-ring');
      setTimeout(() => el.classList.remove('highlight-ring'), 2000);
    }
  }
};
els.fifoOverride.onclick = () => {
  if (pendingOverride) updateOrderStatus(pendingOverride.id, pendingOverride.status, true);
};

window.cancelTicketConfirm = (btn, id) => {
  twoTapConfirm(btn, 'Cancel', 'Confirm?', 'btn-red', () => updateOrderStatus(id, 'cancelled'));
};

window.toggleItemStatus = async (id, currentStatus) => {
  if (currentStatus === 'served' || currentStatus === 'cancelled') return;
  const next = (currentStatus === 'queued' || currentStatus === 'preparing') ? 'ready' : 'preparing';
  try {
    await api(`/order-items/${id}/status`, 'PATCH', { status: next });
    await fetchKdsQueue();
  } catch(e) { showToast(e.error, true); }
};

function updateTimers() {
  document.querySelectorAll('.kds-timer').forEach(el => {
    if (el.dataset.status === 'ready') {
      el.textContent = window.t ? window.t('READY ✓') : 'READY ✓';
      el.className = 'kds-timer tabular timer-green';
      return;
    }
    const created = new Date(el.dataset.created).getTime();
    const estSec = parseInt(el.dataset.est);
    const elapsed = Math.floor((Date.now() - created) / 1000);
    const m = Math.floor(elapsed / 60);
    const s = elapsed % 60;
    
    let remaining = estSec - elapsed;
    if (remaining < 0) remaining = 0;
    const rm = Math.floor(remaining / 60);
    const rs = remaining % 60;
    
    el.textContent = `${m}:${s.toString().padStart(2, '0')} (${rm}${window.t ? window.t('m') : 'm'} ${window.t ? window.t('left') : 'left'})`;
    const sla = el.dataset.sla;
    el.className = `kds-timer tabular ${sla === 'on_time' ? 'timer-green' : sla === 'overdue' ? 'timer-red' : 'timer-amber'}`;
  });

  if (state.customerOrder && els.customerTracker.style.display !== 'none') {
    const created = new Date(state.customerOrder.created_at).getTime();
    let maxPrep = 0;
    state.customerOrder.items.forEach(i => { if(i.prep_time_min > maxPrep) maxPrep = i.prep_time_min; });
    const estSec = maxPrep * 60;
    const elapsed = Math.floor((Date.now() - created) / 1000);
    let remaining = estSec - elapsed;
    if (remaining < 0) remaining = 0;
    const rm = Math.floor(remaining / 60);
    const rs = remaining % 60;
    els.customerTrackerEta.textContent = `${rm}:${rs.toString().padStart(2, '0')}`;
  }
}

// --- CUSTOMER --- //
function renderCustomerTables() {
  const t = state.tables.map(table => `<option value="${table.id}">${escapeHTML(formatTableName(table.name))}</option>`).join('');
  els.customerTableSelect.innerHTML = `<option value="">Select Table...</option>${t}`;
  if (state.customerTableId) {
    els.customerTableSelect.value = String(state.customerTableId);
  }
}
els.customerTableSelect.onchange = (e) => {
  state.customerTableId = e.target.value;
  if (state.customerTableId) {
    els.customerMain.style.display = 'flex';
    state.customerCart = [];
    renderCart(els.customerCartItems, null, false);
    renderMenuItems(els.customerMenuGrid, false);
    fetchCustomerOrder();
  } else {
    els.customerMain.style.display = 'none';
  }
  updateMobileCartBar();
};

async function fetchCustomerOrder() {
  if (!state.customerTableId) return;
  try {
    const tableOrders = await api(`/orders?tableId=${state.customerTableId}`);
    const active = tableOrders.find(o => !['served', 'paid', 'cancelled'].includes(o.status));
    if (active) {
      state.customerOrder = active;
      els.customerTracker.style.display = 'block';
      els.customerTrackerStatus.textContent = active.status.toUpperCase();
    } else {
      els.customerTracker.style.display = 'none';
      state.customerOrder = null;
    }
  } catch(e) {}
}

els.customerSendOrderBtn.onclick = async () => {
  if (!state.customerTableId || state.customerCart.length === 0) return;
  try {
    await api('/orders', 'POST', {
      tableId: parseInt(state.customerTableId),
      userId: state.currentUser?.id || 1,
      source: 'customer',
      notes: '',
      items: state.customerCart.map(c => ({ itemId: c.item.id, quantity: c.qty, notes: c.note }))
    });
    state.customerCart = [];
    renderCart(els.customerCartItems, null, false);
    closeCartDrawers();
    showToast('Your order has been sent!');
    await Promise.all([
      fetchTables(),
      fetchCustomerOrder(),
      fetchKdsQueue()
    ]);
    renderMenuItems(els.customerMenuGrid, false);
  } catch(e) { showToast(e.error, true); }
};

// --- MANAGER --- //
function renderManager() {
  if (!state.analytics) return;
  els.managerRevenue.textContent = formatMoney(state.analytics.revenueToday || 0);
  els.managerPaidOrders.textContent = state.analytics.paidOrdersCount || 0;
  els.managerActiveOrders.textContent = state.analytics.activeOrders || 0;
  els.managerAvgPrep.textContent = `${Math.round(state.analytics.avgPrepTimeMin || 0)}m`;
  
  els.managerRankingList.innerHTML = (state.analytics.topItems || []).map((ti, i) => `
    <div style="display:flex; justify-content:space-between; margin-bottom: 12px; border-bottom:1px dashed var(--color-gray-200); padding-bottom:8px;">
      <span>${i+1}. ${escapeHTML(ti.name)}</span>
      <strong class="tabular" style="color:var(--color-amber-dark)">${ti.order_count} sold</strong>
    </div>
  `).join('');

  renderManagerMenu();
  renderManagerTables();
}

function renderManagerMenu() {
  els.managerMenuTbody.innerHTML = state.menuItems.map(i => `
    <tr>
      <td style="font-weight:bold;">${escapeHTML(i.name)}</td>
      <td>${formatCategoryName(state.categories.find(c => c.id === i.category_id)?.name || '')}</td>
      <td class="tabular"><input type="number" class="inline-edit" value="${i.price}" onchange="updateMenuItem(${i.id}, 'price', this.value)" /></td>
      <td class="tabular"><input type="number" class="inline-edit" value="${i.average_make_time}" onchange="updateMenuItem(${i.id}, 'average_make_time', this.value)" /></td>
      <td>
        <label class="toggle-label"><input type="checkbox" ${i.available ? 'checked' : ''} onchange="updateMenuItem(${i.id}, 'available', this.checked ? 1 : 0)"> Stock</label>
      </td>
      <td><button class="btn-icon" style="color:var(--color-red)" onclick="deleteMenuItem(this, ${i.id})">🗑</button></td>
    </tr>
  `).join('');
}

function renderManagerTables() {
  els.managerTablesTbody.innerHTML = state.tables.map(t => `
    <tr>
      <td style="font-weight:bold;">${escapeHTML(formatTableName(t.name))}</td>
      <td><span class="badge badge-${t.stage}">${escapeHTML(t.status)}</span> ${t.guest_count > 0 ? `• ${t.guest_count} 👤` : ''}</td>
      <td><button type="button" class="btn-icon" style="color:var(--color-red)" onclick="deleteTable(this, ${t.id})">🗑</button></td>
    </tr>
  `).join('');
}

window.updateMenuItem = async (id, field, value) => {
  try { 
    await api(`/menu/items/${id}`, 'PATCH', { [field]: field === 'price' || field === 'average_make_time' ? parseInt(value) : value }); 
    showToast('Item updated');
    fetchMenu();
  } catch(e) { showToast(e.error, true); fetchMenu(); }
};

window.deleteMenuItem = (btn, id) => {
  twoTapConfirm(btn, '🗑', 'Tap again', 'btn-red', async () => {
    try { await api(`/menu/items/${id}`, 'DELETE'); showToast('Deleted'); fetchMenu(); } catch(e) { showToast(e.error, true); }
  });
};
window.deleteTable = (btn, id) => {
  twoTapConfirm(btn, '🗑', 'Tap again', 'btn-red', async () => {
    try { await api(`/tables/${id}`, 'DELETE'); showToast('Deleted'); fetchTables(); } catch(e) { showToast(e.error, true); }
  });
};

els.btnAddMenuItem.onclick = () => els.modalMenuForm.classList.add('active');
els.modalMenuCancel.onclick = () => els.modalMenuForm.classList.remove('active');
els.formAddMenu.onsubmit = async (e) => {
  e.preventDefault();
  try {
    await api('/menu/items', 'POST', {
      name: document.getElementById('add-menu-name').value,
      category_id: parseInt(document.getElementById('add-menu-category').value),
      price: parseInt(document.getElementById('add-menu-price').value),
      average_make_time: parseInt(document.getElementById('add-menu-prep').value),
      station: document.getElementById('add-menu-station').value
    });
    els.modalMenuForm.classList.remove('active');
    els.formAddMenu.reset();
    fetchMenu();
  } catch(e) { showToast(e.error, true); }
};

els.btnAddTable.onclick = () => els.modalTableForm.classList.add('active');
els.modalTableCancel.onclick = () => els.modalTableForm.classList.remove('active');
els.formAddTable.onsubmit = async (e) => {
  e.preventDefault();
  try {
    await api('/tables', 'POST', {
      name: document.getElementById('add-table-name').value
    });
    els.modalTableForm.classList.remove('active');
    els.formAddTable.reset();
    fetchTables();
  } catch(e) { showToast(e.error || 'Endpoint not implemented', true); }
};

window.addEventListener('lang-changed', () => {
  renderTables();
  renderMenuCategories(els.wsMenuCategories, true);
  renderMenuCategories(els.customerCategories, false);
  renderMenuItems(els.wsMenuGrid, true);
  renderMenuItems(els.customerMenuGrid, false);
  if (state.activeView === 'waiter') {
    renderCart(els.wsCartItems, els.wsCartPrep, true);
  } else if (state.activeView === 'customer') {
    renderCart(els.customerCartItems, null, false);
  }
  updateMobileCartBar();
  renderKds();
  renderCustomerTables();
  if (state.activeView === 'manager') renderManager();
  if (state.selectedTableId) refreshSelectedTableUI(state.selectedTableId);
});

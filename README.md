<img alt="OrderIt" src="https://github.com/LeeU1911/OrderIt/blob/master/Designs/icon/nsr-app_icon.png" width="250" height="250"></img>
# OrderIt -- Order without a hitch!

OrderIt is an end-to-end solution for restaurants, coffee shops and food stores to order foods, beverages or other items in an intuitive way. The repository contains an Android client and an Express.js kitchen module.

At OrderIt, we aims to streamline ordering process for business owners so they can deliver more in less time.

## Top features
* Order by an app on Android/iOS smartphone or tablet
* Customer orders are communicated directly to kitchen's system
* Food prepration time is communicated so customers know when their food is prepared and when they should expect it to be served
* First come first serve orders are ensured. Had never a first-come customer has to see others' food being served before them.

## Getting started

### Modern Web App & Backend (`orderit-web`)

A complete, self-contained mobile-first web application and Express/SQLite backend (Waiter POS, FIFO Kitchen Display System, Customer QR Self-Order, and Manager Console with bilingual Tiếng Việt / English support) lives in [`orderit-web`](orderit-web/):

```bash
cd orderit-web
npm install
npm run dev   # http://localhost:3001
```

See [`orderit-web/README.md`](orderit-web/README.md) for demo accounts and test instructions (`npm test`).

### Android application

The Android source lives in the `OrderIt` folder. Open the project with Android Studio Giraffe (or newer) and let Gradle download the dependencies.

### Kitchen module

The Node.js based kitchen service resides in [`kitchen-module`](kitchen-module/). Follow the instructions in its [README](kitchen-module/README.md) to configure Firebase and start the server.

## Talk to us on facebook: https://www.facebook.com/DinoGroup.com.vn

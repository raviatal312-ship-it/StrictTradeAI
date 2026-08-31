# Secure server

Run:
`pip install -r requirements.txt`
`uvicorn app.main:app --host 0.0.0.0 --port 8000`

Set environment variables from `.env.example`.

## Kotak Neo
The server keeps broker credentials off the Android device. The current Kotak Neo v2 flow uses an access token, TOTP login and MPIN validation, returning a baseUrl/session for later API calls. The exact production adapter should be kept in this server only.

## Option chain
Important: Kotak Neo currently states that a direct Option Chain API is unavailable/not allowed. Therefore this project does NOT pretend that endpoint exists. A production strike engine should build the chain from the permitted scrip master/instrument list plus live quotes, or use another authorized data source.

## Paper trading
`PAPER_TRADING=true` is the default. The `/api/paper/order` endpoint records simulated orders only.

## Notifications
Telegram is sent from the server so the bot token is not placed in the APK.

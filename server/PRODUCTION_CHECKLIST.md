# Production checklist

1. Deploy server behind HTTPS.
2. Store Kotak Neo secrets only in server environment/secret manager.
3. Configure Kotak Neo Access Token + TOTP flow on server.
4. Obtain permitted live quotes and instrument/scrip-master files.
5. Build option-chain derived view (Kotak Neo direct Option Chain API is currently unavailable/ not allowed).
6. Add WebSocket/streaming updates where permitted.
7. Add Telegram bot credentials on server.
8. Keep Paper Trading ON until end-to-end tests pass.
9. Add hard limits: max daily loss, max position size, duplicate-signal guard, stale-data guard, kill switch.
10. Only then consider enabling real-money execution with current broker/SEBI/exchange requirements.

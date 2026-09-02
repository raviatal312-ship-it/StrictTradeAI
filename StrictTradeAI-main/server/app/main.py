import os, math, asyncio
from typing import Optional
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import httpx

app=FastAPI(title="StrictTrade AI Secure Server",version="2.0")

PAPER=os.getenv("PAPER_TRADING","true").lower()=="true"
SCORE_MIN=int(os.getenv("STRICT_SCORE_MIN","80"))

class Signal(BaseModel):
    instrument:str
    spot:float
    side:str
    strike:Optional[float]=None
    premium:Optional[float]=None
    score:int
    reason:str

class PaperOrder(BaseModel):
    symbol:str
    side:str
    quantity:int
    price:float

@app.get("/health")
def health():
    return {"ok":True,"paper_trading":PAPER,"service":"stricttrade-ai"}

@app.get("/api/status")
def status():
    return {"paper_trading":PAPER,"strict_score_min":SCORE_MIN,
            "kotak":"adapter-ready","option_chain":"derived-from-scripmaster/quotes; no direct Kotak option-chain API"}

@app.post("/api/paper/order")
def paper_order(o:PaperOrder):
    if not PAPER:
        raise HTTPException(403,"Paper trading is disabled")
    return {"accepted":True,"mode":"PAPER","order":o.model_dump()}

def strict_score(conditions):
    return round(sum(bool(x) for x in conditions)/len(conditions)*100)

@app.post("/api/signal/evaluate")
def evaluate(payload:dict):
    trend=payload.get("trend")=="bullish"
    candle="bull" in payload.get("candle","").lower()
    vwap=payload.get("vwap")=="above"
    rsi=float(payload.get("rsi",50))
    volume=payload.get("volume")=="confirm"
    fib=payload.get("fib_ok",False)
    level=payload.get("level_ok",False)
    oi=payload.get("oi_ok",False)
    rr=float(payload.get("rr",0))
    side="CALL" if trend and candle and vwap and rsi>50 else "NONE"
    checks=[trend,candle,vwap,rsi>50,volume,fib,level,oi,rr>=2]
    score=strict_score(checks)
    if side=="NONE" or score<SCORE_MIN:
        return {"side":"NONE","score":score,"reason":"Strict confirmations incomplete"}
    return {"side":side,"score":score,"reason":"Strict confirmations passed; strike selector should use live quotes/scripmaster"}

# Kotak v2 adapter intentionally isolated. Credentials never returned to client.
async def kotak_login(access_token:str,mobile:str,ucc:str,totp:str):
    headers={"Authorization":access_token,"Content-Type":"application/json"}
    async with httpx.AsyncClient(timeout=15) as c:
        r=await c.post("https://mis.kotaksecurities.com/login/1.0/tradeApiLogin",
                       headers=headers,json={"mobileNumber":mobile,"ucc":ucc,"totp":totp})
        r.raise_for_status()
        return r.json()

@app.post("/api/notify/test")
async def notify_test(message:str="StrictTrade AI test"):
    token=os.getenv("TELEGRAM_BOT_TOKEN"); chat=os.getenv("TELEGRAM_CHAT_ID")
    if not token or not chat: return {"sent":False,"reason":"Telegram not configured"}
    url=f"https://api.telegram.org/bot{token}/sendMessage"
    async with httpx.AsyncClient(timeout=10) as c:
        r=await c.post(url,data={"chat_id":chat,"text":message})
    return {"sent":r.is_success}

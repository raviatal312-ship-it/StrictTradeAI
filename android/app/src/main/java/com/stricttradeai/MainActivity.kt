package com.stricttradeai

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var content: LinearLayout
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        build()
        showDashboard()
    }

    private fun build() {
        val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setBackgroundColor(Color.rgb(10,15,25))}
        val header=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(24,20,24,12)}
        val title=TextView(this).apply{text="STRICTTRADE AI";textSize=25f;setTextColor(Color.WHITE)}
        header.addView(title)
        status=TextView(this).apply{text="● PAPER MODE  •  Server disconnected";textSize=13f;setTextColor(Color.LTGRAY)}
        header.addView(status)
        root.addView(header)
        content=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(18,8,18,18)}
        val scroll=ScrollView(this);scroll.addView(content);root.addView(scroll,LinearLayout.LayoutParams(-1,0,1f))

        val nav=LinearLayout(this).apply{orientation=LinearLayout.HORIZONTAL;setPadding(6,6,6,8)}
        val tabs=listOf("Home","Live","Chain","Signal","Paper")
        tabs.forEachIndexed{ i,t->
            val b=Button(this).apply{text=t;setTextSize(11f)}
            b.setOnClickListener{when(i){0->showDashboard();1->showLive();2->showChain();3->showSignal();4->showPaper()}}
            nav.addView(b,LinearLayout.LayoutParams(0,58,1f))
        }
        root.addView(nav)
        setContentView(root)
    }

    private fun clear(){content.removeAllViews()}
    private fun label(t:String,size:Float=15f):TextView=TextView(this).apply{text=t;textSize=size;setTextColor(Color.WHITE);setPadding(4,10,4,10)}
    private fun card(t:String):TextView=TextView(this).apply{
        text=t;textSize=15f;setTextColor(Color.WHITE);setPadding(18,18,18,18)
        setBackgroundColor(Color.rgb(25,32,46));layoutParams=LinearLayout.LayoutParams(-1,-2).apply{setMargins(0,6,0,6)}
    }
    private fun btn(t:String,fn:()->Unit):Button=Button(this).apply{text=t;setOnClickListener{fn()}}

    private fun showDashboard(){
        clear()
        content.addView(label("MARKET DASHBOARD",22f))
        content.addView(card("NIFTY 50\n24,000.00   +0.42%\n\nBankNifty     —\nMCX             —\n\nMarket status: PAPER / DEMO"))
        content.addView(card("STRICT ENGINE\nScore: 87 / 100\nTrend: Bullish\nVWAP: Above\nRSI: 55\nVolume: Confirmed\nFibonacci: 61.8% support\nOI: CE buildup"))
        content.addView(card("LATEST SIGNAL\n🟢 CALL BUY\nRecommended: 24000 CE\nEntry: price trigger required\nSL: demo\nT1 / T2: demo"))
        content.addView(btn("Connect Secure Server"){checkServer()})
        content.addView(btn("Open Kotak Neo Setup"){kotakSetup()})
    }

    private fun showLive(){
        clear();content.addView(label("LIVE MARKET DATA",22f))
        content.addView(card("NIFTY 50\nLTP: 24,000.00\nChange: +100.00 (+0.42%)\n\nData source: Kotak Neo server adapter\nConnection: PAPER / DEMO"))
        content.addView(card("WATCHLIST\nNIFTY 50      24,000\nBANKNIFTY     —\nRELIANCE      —\nTCS           —\nGOLD / MCX    —"))
        content.addView(btn("Refresh Live Data"){Toast.makeText(this,"Live feed requires configured server + Kotak session.",Toast.LENGTH_LONG).show()})
    }

    private fun showChain(){
        clear();content.addView(label("OPTION CHAIN",22f))
        content.addView(card("NIFTY • Spot 24,000\n\nStrike      CE LTP     CE OI       PE LTP     PE OI\n23800       310        15L         12         4L\n23900       250        18L         18         6.5L\n24000       180        22L         30         13L\n24100       120        16L         55         20L\n24200        75        11L         90         26L\n24300        45         8L        135         29L\n\nExact strike engine: READY"))
        content.addView(label("Note: Kotak Neo currently does not expose a direct Option Chain API; production chain must be derived from permitted instrument/scrip-master + live quote data or another authorized source.",13f))
    }

    private fun showSignal(){
        clear();content.addView(label("STRICT SIGNAL",22f))
        content.addView(card("🟢 CALL BUY\n\nNIFTY: 24,000\nRecommended Strike: 24000 CE\n\nStrict Score: 87 / 100\n\nCONFIRMATIONS\n✓ Trend\n✓ Candlestick\n✓ VWAP\n✓ RSI\n✓ Volume\n✓ Fibonacci\n✓ Support\n✓ OI\n✓ R:R >= 1:2"))
        content.addView(card("ORDER PLAN — PAPER\nEntry: Trigger confirmation\nSL: Premium -22%\nTarget 1: Premium +20%\nTarget 2: Premium +45%\n\nReal-money execution: OFF"))
        content.addView(btn("Send Telegram Test"){Toast.makeText(this,"Telegram test requires server bot configuration.",Toast.LENGTH_LONG).show()})
    }

    private fun showPaper(){
        clear();content.addView(label("PAPER TRADING",22f))
        content.addView(card("PAPER BALANCE\n₹1,00,000.00\nToday's P&L: ₹0.00\nOpen positions: 0"))
        content.addView(card("ORDER TICKET\nSymbol: NIFTY 24000 CE\nSide: BUY\nQty: 1 lot\nOrder type: LIMIT\nPrice: demo\n\nStatus: WAITING"))
        content.addView(btn("Place Paper Order"){Toast.makeText(this,"Paper order simulated — no real order sent.",Toast.LENGTH_SHORT).show()})
        content.addView(card("ORDER HISTORY\nNo paper orders yet."))
    }

    private fun kotakSetup(){
        val w=LinearLayout(this).apply{
    orientation=LinearLayout.VERTICAL
    setPadding(20,20,20,20)
}
        val token=EditText(this).apply{hint="Access Token";inputType=129}
        w.addView(token)
        w.addView(EditText(this).apply{hint="UCC / Client Code"})
        w.addView(EditText(this).apply{hint="Mobile Number"})
        w.addView(label("TOTP/MPIN should be handled only through the secure server. Never hard-code secrets in the APK.",13f))
        AlertDialogHelper.show(this,"Kotak Neo API Setup",w)
    }

    private fun checkServer(){
        val url="https://YOUR-SERVER/health"
        OkHttpClient().newCall(Request.Builder().url(url).build()).enqueue(object:Callback{
            override fun onFailure(c:Call,e:IOException)=runOnUiThread{status.text="● PAPER MODE  •  Server unavailable"}
            override fun onResponse(c:Call,r:Response)=runOnUiThread{status.text=if(r.isSuccessful)"● PAPER MODE • Server connected" else "● PAPER MODE • HTTP ${r.code}"}
        })
    }
}

object AlertDialogHelper{
    fun show(a:android.app.Activity,title:String,v:View){
        android.app.AlertDialog.Builder(a).setTitle(title).setView(v).setPositiveButton("Save"){d,_->
            Toast.makeText(a,"Credentials are not persisted by this UI.",Toast.LENGTH_SHORT).show()
        }.setNegativeButton("Cancel",null).show()
    }
}

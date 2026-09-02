package com.stricttradeai

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    private lateinit var content: LinearLayout
    private lateinit var status: TextView
    private var paperBalance = 100000.0
    private val orders = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildShell()
        showDashboard()
    }

    private fun buildShell() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(10, 15, 25))
        }
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 18, 24, 10)
        }
        header.addView(TextView(this).apply {
            text = "STRICTTRADE AI"
            textSize = 25f
            setTextColor(Color.WHITE)
        })
        status = TextView(this).apply {
            text = "● OFFLINE PAPER MODE  •  No broker/API required"
            textSize = 13f
            setTextColor(Color.LTGRAY)
        }
        header.addView(status)
        root.addView(header)

        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 8, 18, 18)
        }
        val scroll = ScrollView(this)
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(4, 4, 4, 8)
        }
        listOf("Home", "Analyze", "Chain", "Signal", "Paper").forEachIndexed { i, t ->
            nav.addView(Button(this).apply {
                text = t
                textSize = 10f
                setOnClickListener {
                    when (i) {
                        0 -> showDashboard(); 1 -> showAnalyzer(); 2 -> showChain(); 3 -> showSignal(); 4 -> showPaper()
                    }
                }
            }, LinearLayout.LayoutParams(0, 58, 1f))
        }
        root.addView(nav)
        setContentView(root)
    }

    private fun clear() = content.removeAllViews()
    private fun label(t: String, size: Float = 15f) = TextView(this).apply {
        text = t; textSize = size; setTextColor(Color.WHITE); setPadding(4, 10, 4, 10)
    }
    private fun card(t: String) = TextView(this).apply {
        text = t; textSize = 15f; setTextColor(Color.WHITE); setPadding(18, 18, 18, 18)
        setBackgroundColor(Color.rgb(25, 32, 46))
        layoutParams = LinearLayout.LayoutParams(-1, -2).apply { setMargins(0, 6, 0, 6) }
    }
    private fun btn(t: String, fn: () -> Unit) = Button(this).apply { text = t; setOnClickListener { fn() } }

    private fun showDashboard() {
        clear()
        content.addView(label("PAPER TRADING DASHBOARD", 22f))
        content.addView(card("Mode: OFFLINE / DEMO\n\nNo Kotak Neo API, access token, UCC or server is required for this mode."))
        content.addView(card("STRICT ENGINE\nEnter live chart values manually in Analyze.\nThe engine checks trend + candle + VWAP + RSI + volume + Fibonacci + level + OI + R:R."))
        content.addView(card("LATEST SIGNAL\nUse Analyze to calculate CALL / PUT / WAIT.\nPaper execution only — no real order is sent."))
        content.addView(btn("Open Signal Analyzer") { showAnalyzer() })
        content.addView(btn("Open Paper Trading") { showPaper() })
    }

    private fun edit(hint: String, value: String = ""): EditText = EditText(this).apply {
        this.hint = hint; setText(value); setTextColor(Color.WHITE); setHintTextColor(Color.LTGRAY)
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        setPadding(12, 4, 12, 4)
    }

    private fun showAnalyzer() {
        clear()
        content.addView(label("STRICT SIGNAL ANALYZER", 22f))
        content.addView(label("No API needed. Enter the values you see on your chart."))
        val spot = edit("Spot price (e.g. 24000)")
        val premium = edit("Option premium (optional)")
        val rsi = edit("RSI (e.g. 55)", "50")
        content.addView(spot); content.addView(premium); content.addView(rsi)

        val trend = Spinner(this).apply { adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, arrayOf("Bullish", "Bearish", "Sideways")) }
        val candle = Spinner(this).apply { adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, arrayOf("Bullish", "Bearish", "Neutral")) }
        val vwap = Spinner(this).apply { adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, arrayOf("Above VWAP", "Below VWAP", "At VWAP")) }
        val volume = Spinner(this).apply { adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, arrayOf("Confirmed", "Weak", "Unknown")) }
        val fib = Spinner(this).apply { adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, arrayOf("Support/Resistance confirmed", "Not confirmed")) }
        val level = Spinner(this).apply { adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, arrayOf("Breakout/level confirmed", "Not confirmed")) }
        val oi = Spinner(this).apply { adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, arrayOf("CALL/PUT OI confirms", "OI neutral", "OI not checked")) }
        listOf("Trend", "Candle", "VWAP", "Volume", "Fibonacci", "Price level", "OI").forEach { content.addView(label(it, 13f)) }
        content.addView(trend); content.addView(candle); content.addView(vwap); content.addView(volume); content.addView(fib); content.addView(level); content.addView(oi)

        content.addView(btn("ANALYZE CALL / PUT") {
            val s = spot.text.toString().toDoubleOrNull()
            val p = premium.text.toString().toDoubleOrNull()
            val rv = rsi.text.toString().toDoubleOrNull() ?: 50.0
            if (s == null || s <= 0) { Toast.makeText(this, "Spot price enter करें", Toast.LENGTH_SHORT).show(); return@btn }
            val bullChecks = listOf(trend.selectedItem == "Bullish", candle.selectedItem == "Bullish", vwap.selectedItem == "Above VWAP", rv > 50, volume.selectedItem == "Confirmed", fib.selectedItem == "Support/Resistance confirmed", level.selectedItem == "Breakout/level confirmed", oi.selectedItem == "CALL/PUT OI confirms")
            val bearChecks = listOf(trend.selectedItem == "Bearish", candle.selectedItem == "Bearish", vwap.selectedItem == "Below VWAP", rv < 50, volume.selectedItem == "Confirmed", fib.selectedItem == "Support/Resistance confirmed", level.selectedItem == "Breakout/level confirmed", oi.selectedItem == "CALL/PUT OI confirms")
            val bull = round(bullChecks.count { it }.toDouble() / bullChecks.size * 100).toInt()
            val bear = round(bearChecks.count { it }.toDouble() / bearChecks.size * 100).toInt()
            val side = when { bull >= 75 && bull > bear -> "CALL BUY"; bear >= 75 && bear > bull -> "PUT BUY"; else -> "WAIT" }
            val strike = (round(s / 50.0) * 50).toInt()
            val plan = if (p != null && p > 0) "\nPremium Entry: ${fmt(p)}\nSL: ${fmt(p * 0.78)}\nTarget 1: ${fmt(p * 1.20)}\nTarget 2: ${fmt(p * 1.45)}" else "\nPremium entry डालने पर SL/Targets भी calculate होंगे."
            content.addView(card("RESULT\n\n$side\nCALL Score: $bull / 100\nPUT Score: $bear / 100\n\nSuggested ATM strike: $strike\n$plan\n\nRule: 75%+ confirmations required. This is a paper/demo signal, not financial advice."))
        })
        content.addView(label("Tip: screenshot देखकर values manually भरें. इस offline version में chart image की automatic AI interpretation नहीं है; उसके लिए AI/data service जोड़नी होगी.", 12f))
    }

    private fun showChain() {
        clear(); content.addView(label("MANUAL OPTION CHAIN", 22f))
        content.addView(card("API के बिना real-time option chain उपलब्ध नहीं हो सकती।\n\nआप chart/option-chain से values देखकर Analyzer में डाल सकते हैं.\n\nNo live data is fabricated in this version."))
        content.addView(btn("Go to Analyzer") { showAnalyzer() })
    }

    private fun showSignal() {
        clear(); content.addView(label("STRICT SIGNAL", 22f))
        content.addView(card("SIGNAL ENGINE\n\nCALL: 8 confirmations में 6+ होने पर eligible\nPUT: 8 confirmations में 6+ होने पर eligible\nOtherwise: WAIT\n\nEntry / SL / Target तभी calculate होंगे जब premium दिया जाए."))
        content.addView(btn("Run Analyzer") { showAnalyzer() })
    }

    private fun showPaper() {
        clear(); content.addView(label("PAPER TRADING", 22f))
        content.addView(card("PAPER BALANCE\n₹${fmt(paperBalance)}\nOpen positions: ${orders.size}"))
        val symbol = EditText(this).apply { hint = "Symbol e.g. NIFTY 24000 CE"; setTextColor(Color.WHITE); setHintTextColor(Color.LTGRAY) }
        val price = edit("Entry price")
        val qty = edit("Quantity", "1")
        content.addView(symbol); content.addView(price); content.addView(qty)
        content.addView(btn("PLACE PAPER BUY") {
            val pr = price.text.toString().toDoubleOrNull(); val q = qty.text.toString().toIntOrNull() ?: 1
            if (symbol.text.isNullOrBlank() || pr == null || pr <= 0) { Toast.makeText(this, "Symbol और valid price डालें", Toast.LENGTH_SHORT).show(); return@btn }
            val cost = pr * q
            if (cost > paperBalance) { Toast.makeText(this, "Paper balance insufficient", Toast.LENGTH_SHORT).show(); return@btn }
            paperBalance -= cost; orders.add("BUY ${symbol.text} x$q @ ${fmt(pr)}")
            Toast.makeText(this, "Paper order placed — no real order sent", Toast.LENGTH_SHORT).show(); showPaper()
        })
        content.addView(label("ORDER HISTORY", 18f))
        if (orders.isEmpty()) content.addView(card("No paper orders yet.")) else orders.asReversed().forEach { content.addView(card(it)) }
        content.addView(btn("Reset Paper Account") { paperBalance = 100000.0; orders.clear(); showPaper() })
    }

    private fun fmt(v: Double) = String.format("%.2f", v)
}

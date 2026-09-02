package com.stricttradeai

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var content: LinearLayout
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        build()
        showDashboard()
    }

    private fun build() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(10, 15, 25))
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 20, 24, 12)
        }

        val title = TextView(this).apply {
            text = "STRICTTRADE AI"
            textSize = 25f
            setTextColor(Color.WHITE)
        }

        header.addView(title)

        status = TextView(this).apply {
            text = "● OFFLINE PAPER MODE • No broker/API required"
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

        root.addView(
            scroll,
            LinearLayout.LayoutParams(-1, 0, 1f)
        )

        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(6, 6, 6, 8)
        }

        val tabs = listOf(
            "Home",
            "Analyze",
            "Chain",
            "Signal",
            "Paper"
        )

        tabs.forEachIndexed { i, t ->
            val b = Button(this).apply {
                text = t
                setTextSize(11f)
            }

            b.setOnClickListener {
                when (i) {
                    0 -> showDashboard()
                    1 -> showAnalyzer()
                    2 -> showChain()
                    3 -> showSignal()
                    4 -> showPaper()
                }
            }

            nav.addView(
                b,
                LinearLayout.LayoutParams(0, 58, 1f)
            )
        }

        root.addView(nav)
        setContentView(root)
    }

    private fun clear() {
        content.removeAllViews()
    }

    private fun label(
        t: String,
        size: Float = 15f
    ): TextView {
        return TextView(this).apply {
            text = t
            textSize = size
            setTextColor(Color.WHITE)
            setPadding(4, 10, 4, 10)
        }
    }

    private fun card(t: String): TextView {
        return TextView(this).apply {
            text = t
            textSize = 15f
            setTextColor(Color.WHITE)
            setPadding(18, 18, 18, 18)

            setBackgroundColor(
                Color.rgb(25, 32, 46)
            )

            layoutParams =
                LinearLayout.LayoutParams(-1, -2).apply {
                    setMargins(0, 6, 0, 6)
                }
        }
    }

    private fun btn(
        t: String,
        fn: () -> Unit
    ): Button {
        return Button(this).apply {
            text = t
            setOnClickListener {
                fn()
            }
        }
    }

    // ---------------------------------------------------------
    // HOME
    // ---------------------------------------------------------

    private fun showDashboard() {
        clear()

        content.addView(
            label("PAPER TRADING DASHBOARD", 22f)
        )

        content.addView(
            card(
                "OFFLINE MODE\n\n" +
                "No Kotak Neo API required\n" +
                "No broker connection required\n" +
                "No real-money order execution\n\n" +
                "Use ANALYZE for manual market analysis."
            )
        )

        content.addView(
            card(
                "NIFTY 50\n" +
                "Enter live spot manually in Analyze\n\n" +
                "BANKNIFTY\n" +
                "Manual analysis available\n\n" +
                "MCX\n" +
                "Manual analysis available"
            )
        )

        content.addView(
            card(
                "STRICT SIGNAL ENGINE\n\n" +
                "8 confirmation factors:\n" +
                "• Trend\n" +
                "• Candlestick\n" +
                "• VWAP\n" +
                "• RSI\n" +
                "• Volume\n" +
                "• Fibonacci\n" +
                "• Price Level\n" +
                "• OI"
            )
        )

        content.addView(
            btn("OPEN ANALYZER") {
                showAnalyzer()
            }
        )
    }

    // ---------------------------------------------------------
    // ANALYZER
    // ---------------------------------------------------------

    private fun showAnalyzer() {
        clear()

        content.addView(
            label("MARKET ANALYZER", 22f)
        )

        content.addView(
            label(
                "Enter chart values manually. " +
                "The app calculates CALL / PUT confirmation score.",
                13f
            )
        )

        val spot = EditText(this).apply {
            hint = "Spot Price (example: 24000)"
            inputType = 2
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
        }

        val premium = EditText(this).apply {
            hint = "Option Premium (optional)"
            inputType = 2
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
        }

        val rsi = EditText(this).apply {
            hint = "RSI (example: 55)"
            inputType = 2
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
        }

        content.addView(spot)
        content.addView(premium)
        content.addView(rsi)

        val trend = spinner(
            "Trend",
            listOf(
                "Bullish",
                "Bearish",
                "Sideways"
            )
        )

        val candle = spinner(
            "Candlestick",
            listOf(
                "Bullish confirmation",
                "Bearish confirmation",
                "Neutral"
            )
        )

        val vwap = spinner(
            "VWAP",
            listOf(
                "Above VWAP",
                "Below VWAP",
                "Near VWAP"
            )
        )

        val volume = spinner(
            "Volume",
            listOf(
                "Increasing",
                "Decreasing",
                "Normal"
            )
        )

        val fib = spinner(
            "Fibonacci",
            listOf(
                "Support",
                "Resistance",
                "Neutral"
            )
        )

        val priceLevel = spinner(
            "Price Level",
            listOf(
                "Strong Support",
                "Strong Resistance",
                "Neutral"
            )
        )

        val oi = spinner(
            "Open Interest",
            listOf(
                "CALL buildup",
                "PUT buildup",
                "Neutral"
            )
        )

        content.addView(trend)
        content.addView(candle)
        content.addView(vwap)
        content.addView(volume)
        content.addView(fib)
        content.addView(priceLevel)
        content.addView(oi)

        content.addView(
            btn("CALCULATE STRICT SIGNAL") {

                val spotValue =
                    spot.text.toString().toDoubleOrNull()

                val premiumValue =
                    premium.text.toString().toDoubleOrNull()

                val rsiValue =
                    rsi.text.toString().toDoubleOrNull()

                if (spotValue == null) {
                    Toast.makeText(
                        this,
                        "Please enter Spot Price",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@btn
                }

                val values = listOf(
                    trend.selectedItem.toString(),
                    candle.selectedItem.toString(),
                    vwap.selectedItem.toString(),
                    volume.selectedItem.toString(),
                    fib.selectedItem.toString(),
                    priceLevel.selectedItem.toString(),
                    oi.selectedItem.toString()
                )

                var callScore = 0
                var putScore = 0

                // Trend
                if (values[0] == "Bullish") {
                    callScore++
                } else if (values[0] == "Bearish") {
                    putScore++
                }

                // Candle
                if (values[1] == "Bullish confirmation") {
                    callScore++
                } else if (values[1] == "Bearish confirmation") {
                    putScore++
                }

                // VWAP
                if (values[2] == "Above VWAP") {
                    callScore++
                } else if (values[2] == "Below VWAP") {
                    putScore++
                }

                // Volume
                if (values[3] == "Increasing") {
                    if (callScore >= putScore) {
                        callScore++
                    } else {
                        putScore++
                    }
                }

                // Fibonacci
                if (values[4] == "Support") {
                    callScore++
                } else if (values[4] == "Resistance") {
                    putScore++
                }

                // Price level
                if (values[5] == "Strong Support") {
                    callScore++
                } else if (values[5] == "Strong Resistance") {
                    putScore++
                }

                // OI
                if (values[6] == "CALL buildup") {
                    callScore++
                } else if (values[6] == "PUT buildup") {
                    putScore++
                }

                // RSI
                if (rsiValue != null) {
                    if (rsiValue >= 50 && rsiValue <= 70) {
                        callScore++
                    }

                    if (rsiValue >= 30 && rsiValue < 50) {
                        putScore++
                    }
                }

                val maxScore = 8

                val callPercent =
                    ((callScore.toDouble() / maxScore) * 100)
                        .roundToInt()

                val putPercent =
                    ((putScore.toDouble() / maxScore) * 100)
                        .roundToInt()

                val strike =
                    (spotValue / 50.0)
                        .roundToInt() * 50

                val result: String

                if (
                    callPercent >= 75 &&
                    callPercent > putPercent
                ) {
                    result =
                        "CALL BUY\n\n" +
                        "Spot: ${format(spotValue)}\n" +
                        "Suggested Strike: $strike CE\n\n" +
                        "CALL Score: $callPercent%\n" +
                        "PUT Score: $putPercent%\n\n" +
                        "CONFIRMATION: STRONG\n\n" +
                        "Paper trade only."
                } else if (
                    putPercent >= 75 &&
                    putPercent > callPercent
                ) {
                    result =
                        "PUT BUY\n\n" +
                        "Spot: ${format(spotValue)}\n" +
                        "Suggested Strike: $strike PE\n\n" +
                        "CALL Score: $callPercent%\n" +
                        "PUT Score: $putPercent%\n\n" +
                        "CONFIRMATION: STRONG\n\n" +
                        "Paper trade only."
                } else {
                    result =
                        "WAIT / NO TRADE\n\n" +
                        "Spot: ${format(spotValue)}\n\n" +
                        "CALL Score: $callPercent%\n" +
                        "PUT Score: $putPercent%\n\n" +
                        "Required confirmation: 75%+\n\n" +
                        "Avoid forcing an entry."
                }

                content.addView(
                    card(result)
                )

                if (premiumValue != null) {

                    val sl = premiumValue * 0.78
                    val target1 = premiumValue * 1.20
                    val target2 = premiumValue * 1.45

                    content.addView(
                        card(
                            "PAPER OPTION PLAN\n\n" +
                            "Premium Entry: ${format(premiumValue)}\n" +
                            "Stop Loss: ${format(sl)}\n" +
                            "Target 1: ${format(target1)}\n" +
                            "Target 2: ${format(target2)}\n\n" +
                            "Risk/Reward: simulated"
                        )
                    )
                }
            }
        )
    }

    private fun spinner(
        title: String,
        items: List<String>
    ): Spinner {

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val text = TextView(this).apply {
            this.text = title
            textSize = 13f
            setTextColor(Color.LTGRAY)
            setPadding(4, 8, 4, 2)
        }

        layout.addView(text)

        val spinner = Spinner(this)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            items
        )

        spinner.adapter = adapter

        layout.addView(spinner)

        content.addView(layout)

        return spinner
    }

    // ---------------------------------------------------------
    // OPTION CHAIN
    // ---------------------------------------------------------

    private fun showChain() {
        clear()

        content.addView(
            label("OPTION CHAIN", 22f)
        )

        content.addView(
            card(
                "OFFLINE PAPER MODE\n\n" +
                "Real-time Option Chain is disabled because " +
                "no broker/API connection is required in this version.\n\n" +
                "Use the ANALYZE screen to enter Spot, " +
                "Premium, OI and other confirmations manually."
            )
        )

        content.addView(
            card(
                "EXAMPLE PAPER CHAIN\n\n" +
                "Strike     CE        PE\n\n" +
                "23800      ---       ---\n" +
                "23900      ---       ---\n" +
                "24000      ---       ---\n" +
                "24100      ---       ---\n" +
                "24200      ---       ---\n\n" +
                "Enter actual market values manually."
            )
        )
    }

    // ---------------------------------------------------------
    // SIGNAL
    // ---------------------------------------------------------

    private fun showSignal() {
        clear()

        content.addView(
            label("STRICT SIGNAL ENGINE", 22f)
        )

        content.addView(
            card(
                "SIGNAL LOGIC\n\n" +
                "The analyzer checks 8 confirmations:\n\n" +
                "1. Trend\n" +
                "2. Candlestick\n" +
                "3. VWAP\n" +
                "4. RSI\n" +
                "5. Volume\n" +
                "6. Fibonacci\n" +
                "7. Price Level\n" +
                "8. Open Interest\n\n" +
                "Minimum confirmation for a strong signal: 75%."
            )
        )

        content.addView(
            card(
                "SIGNAL TYPES\n\n" +
                "CALL BUY\n" +
                "Bullish confirmations >= 75%\n\n" +
                "PUT BUY\n" +
                "Bearish confirmations >= 75%\n\n" +
                "WAIT\n" +
                "If confirmation is below threshold."
            )
        )

        content.addView(
            btn("OPEN ANALYZER") {
                showAnalyzer()
            }
        )
    }

    // ---------------------------------------------------------
    // PAPER TRADING
    // ---------------------------------------------------------

    private fun showPaper() {
        clear()

        content.addView(
            label("PAPER TRADING", 22f)
        )

        content.addView(
            card(
                "PAPER BALANCE\n\n" +
                "₹1,00,000.00\n\n" +
                "Today's P&L: ₹0.00\n" +
                "Open Positions: 0\n\n" +
                "REAL MONEY: OFF"
            )
        )

        val symbol = EditText(this).apply {
            hint = "Symbol / Strike (example: NIFTY 24000 CE)"
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
        }

        val price = EditText(this).apply {
            hint = "Entry Premium"
            inputType = 2
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
        }

        val qty = EditText(this).apply {
            hint = "Quantity / Lots"
            inputType = 2
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
        }

        content.addView(symbol)
        content.addView(price)
        content.addView(qty)

        content.addView(
            btn("PLACE PAPER ORDER") {

                val s = symbol.text.toString()
                val p = price.text.toString()
                val q = qty.text.toString()

                if (
                    s.isBlank() ||
                    p.isBlank() ||
                    q.isBlank()
                ) {
                    Toast.makeText(
                        this,
                        "Please enter Symbol, Price and Quantity",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@btn
                }

                content.addView(
                    card(
                        "PAPER ORDER PLACED\n\n" +
                        "Symbol: $s\n" +
                        "Side: BUY\n" +
                        "Entry: ₹$p\n" +
                        "Quantity: $q\n\n" +
                        "Status: SIMULATED\n" +
                        "No real order was sent."
                    )
                )

                Toast.makeText(
                    this,
                    "Paper order simulated",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        content.addView(
            card(
                "ORDER HISTORY\n\n" +
                "Orders placed from this screen are simulation only."
            )
        )
    }

    private fun format(value: Double): String {
        return String.format("%.2f", value)
    }
}

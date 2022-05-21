package cn.flutterfirst.weiv_example

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import cn.flutterfirst.weiv.core.WeiV
import cn.flutterfirst.weiv.core.activities.WeiVActivity
import cn.flutterfirst.weiv.core.widgets.Stateful
import cn.flutterfirst.weiv.core.widgets.StatefulWidget
import cn.flutterfirst.weiv.core.widgets.XmlView
import cn.flutterfirst.weiv.wrappers.linearlayout.Flex
import cn.flutterfirst.weiv.wrappers.linearlayout.FlexDirection
import cn.flutterfirst.weiv.wrappers.textview.Text
import cn.flutterfirst.weiv_example.embed.WeiVEmbedCounterJavaActivity
import cn.flutterfirst.weiv_example.embed.WeiVEmbedCounterKotlinActivity
import cn.flutterfirst.weiv_example.ext.Button

class WeiVCounterKotlinActivity : WeiVActivity() {
    private var count = 0
    private val maxCount = 10
    private val minCount = 0
    private var url = "https://baidu.com"

    fun WeiV.moduleItem(index: Int) {
        Button(text = "This button is from module, $index")
    }

    override fun build() = WeiV {
        Flex {
            it.orientation = FlexDirection.VERTICAL

            Button(text = "Add count", enable = count < maxCount, onClick = {
                setState {
                    count++
                }
            })

            Button(text = "Sub count", enable = count > minCount, onClick = {
                setState {
                    count--
                }
            })

            Text(text = "count = $count")

            Stateful(state = object : StatefulWidget.State() {
                private var innerCount = 0

                override fun build() = WeiV {
                    Button(
                        text = "This button maintains state alone, innerCount = $innerCount",
                        onClick = {
                            setState {
                                innerCount++
                            }
                        }
                    )
                }
            })

            repeat(3) {
                moduleItem(it)
            }

            merge {
                WeiV {
                    Text(text = "Merge with outer layer")
                }
            }

            Button(text = "Open weiV counter(Java)", onClick = {
                startActivity(
                    Intent(
                        this@WeiVCounterKotlinActivity,
                        WeiVCounterJavaActivity::class.java
                    )
                )
            })

            Button(text = "Open weiV embed counter(Kotlin)", onClick = {
                startActivity(
                    Intent(
                        this@WeiVCounterKotlinActivity,
                        WeiVEmbedCounterKotlinActivity::class.java
                    )
                )
            })

            Button(text = "Open weiV embed counter(Java)", onClick = {
                startActivity(
                    Intent(
                        this@WeiVCounterKotlinActivity,
                        WeiVEmbedCounterJavaActivity::class.java
                    )
                )
            })

            Button(text = "Change WebView url", onClick = {
                setState {
                    url = "https://flutterfirst.cn"
                }
            })

            XmlView(viewCreator = {
                val webView = WebView(this@WeiVCounterKotlinActivity)
                webView.settings.javaScriptEnabled = true
                webView.layoutParams = ViewGroup.LayoutParams(-1, -1)
                webView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                        if (url.startsWith("http")) {
                            return false
                        } else {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            this@WeiVCounterKotlinActivity.startActivity(intent)
                            return true
                        }
                    }
                }
                return@XmlView webView
            }, param = url, onParamChanged = { webView, url, first ->
                if (first) {
                    webView.loadUrl(url!!)
                    webView.setTag(R.id.current_url, url)
                } else {
                    val currentUrl = webView.getTag(R.id.current_url)
                    if (currentUrl != url) {
                        webView.loadUrl(url!!)
                        webView.setTag(R.id.current_url, url)
                    }
                }
            })
        }
    }
}
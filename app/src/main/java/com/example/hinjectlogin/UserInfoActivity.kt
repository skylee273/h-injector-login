package com.example.hinjectlogin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.hinjectlogin.ui.theme.HInjectLoginTheme
import kotlinx.coroutines.launch

class UserInfoActivity : ComponentActivity() {

    // private val localDataSource = UserLocalDataSource(this)

    private val localDataSource : UserLocalDataSource by lazy {
        (application as App).appContainer.createUserLocalDataSource()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            HInjectLoginTheme {
                Surface {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            var token by remember { mutableStateOf("") }
                            LaunchedEffect(Unit) {
                                launch{
                                    token = localDataSource.getToken().orEmpty()
                                }
                            }
                            Text(text = "$token")
                            Spacer(modifier = Modifier.height(20.dp))
                            TextButton(onClick = {
                                lifecycleScope.launch { 
                                    localDataSource.clear()
                                    startActivity(Intent(this@UserInfoActivity, LoginActivity::class.java))
                                    finish()
                                }
                            }) {
                                Text(text = "Logout")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    fun UserInfoPreview() {
        HInjectLoginTheme {
            Surface {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Token: example_token")
                        Spacer(modifier = Modifier.height(20.dp))
                        TextButton(onClick = {}) {
                            Text(text = "Logout")
                        }
                    }
                }
            }
        }
    }
}
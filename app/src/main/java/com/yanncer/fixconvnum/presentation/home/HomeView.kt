package com.yanncer.fixconvnum.presentation.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yanncer.fixconvnum.presentation.components.CustomFilledButton
import com.yanncer.fixconvnum.presentation.components.CustomProgress
import com.yanncer.fixconvnum.presentation.components.FilledTextField
import com.yanncer.fixconvnum.presentation.infos.InfosView
import com.yanncer.fixconvnum.presentation.ui.theme.AccentColor
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterial3Api
@Composable
fun HomeView(
    navController: NavController? = null,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val buttonContent = if (state.isLoading) {
        "Veuillez patienter..."
    } else if (viewModel.issueExistsInList()) {
        "Corriger les contacts"
    } else {
        "Tous vos contacts sont corrects"
    }

    val backgroundColor = if (state.isLoading) {
        AccentColor.copy(alpha = 0.1f)
    } else if (viewModel.issueExistsInList()) {
        AccentColor
    } else {
        AccentColor.copy(alpha = 0.1f)
    }


    HandleContactPermission(
        onPermissionGranted = { viewModel.fetchContacts() },
        onPermissionDenied = {

        },
        context = LocalContext.current
    )

    ///WRITE PERMISSIONS HANDLING
    var permissionState by remember { mutableStateOf<PermissionState>(PermissionState.Idle) }
    val context = LocalContext.current

    // Launcher for permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionState = when {
                isGranted -> PermissionState.Granted
                else -> PermissionState.Denied
            }
        }
    )

    //dialog of permission Writasble
    if (permissionState is PermissionState.Denied) {
        PermissionRationaleDialog(
            onConfirm = {
                permissionLauncher.launch(Manifest.permission.WRITE_CONTACTS)
            },
            onCancel = {
                permissionState = PermissionState.Idle
            }
        )
    }


    val snackbarHostState = remember {
        SnackbarHostState()
    }


    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is HomeViewModel.UIEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

            }
        }
    }


    Scaffold(containerColor = Color.White, snackbarHost = {
        SnackbarHost(snackbarHostState)
    }, topBar = {
        TopAppBar(title = {
            Text(
                text = "Mes contacts", style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
        }, actions = {
            IconButton(onClick = {
                showBottomSheet = true

            }) {
                Icon(Icons.Outlined.Info, contentDescription = "Information about app")
            }

            IconButton(onClick = { }) {
                Icon(Icons.Outlined.MoreVert, contentDescription = "More")
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
        )

    }) { contentPadding ->


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 20.dp)

        ) {
            Column(

            ) {

                FilledTextField(text = state.query, onValueChange = {
                    viewModel.onQueryChange(it)
                }, placeHolder = "Rechercher un nom ou prénom")


                // Spacer(modifier = Modifier.height(20.dp))

                if (state.isLoading && state.contacts.isEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    CustomProgress(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                LazyColumn(contentPadding = PaddingValues(bottom = 80.dp, top = 30.dp)) {

                    items(state.contacts) { contact ->
                        ContactItem(contact)
                    }
                }

            }

            Box(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .background(color = Color.White)
                    .align(Alignment.BottomCenter)


            ) {
                CustomFilledButton(
                    text = buttonContent,
                    onClick = {
                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.WRITE_CONTACTS
                            ) == PackageManager.PERMISSION_GRANTED -> {
                                viewModel.updateContacts()
                            }

                            shouldShowRequestPermissionRationale(
                                context as Activity,
                                Manifest.permission.WRITE_CONTACTS
                            ) -> {
                                // show rational of permission
                                permissionState = PermissionState.Denied
                            }

                            else -> {
                                // ask for permission
                                permissionLauncher.launch(Manifest.permission.WRITE_CONTACTS)
                            }
                        }
                    },
                    color = backgroundColor,
                    textColor = if (viewModel.issueExistsInList()) Color.White else AccentColor,
                )

            }


        }

        if (showBottomSheet) {

            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState, containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)

            ) {
                // Sheet content
                InfosView()
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PrevHomeView() {
    HomeView()
}
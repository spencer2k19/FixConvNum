package com.yanncer.fixconvnum.presentation.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yanncer.fixconvnum.R
import com.yanncer.fixconvnum.common.BeninPhoneValidator.hasPhoneNumberIssue
import com.yanncer.fixconvnum.domain.models.Contact
import com.yanncer.fixconvnum.presentation.components.CustomFilledButton
import com.yanncer.fixconvnum.presentation.components.CustomProgress
import com.yanncer.fixconvnum.presentation.components.FilledTextField
import com.yanncer.fixconvnum.presentation.infos.InfosView
import com.yanncer.fixconvnum.presentation.ui.theme.AccentColor
import com.yanncer.fixconvnum.presentation.ui.theme.AccentDarkColor
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterial3Api
@Composable
fun HomeView(
    navController: NavController? = null,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val showRemoveDialogState = viewModel.showRemoveDialogState.value
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }


    val buttonContent = if (state.isLoading) {
        "Veuillez patienter..."
    } else if (viewModel.issueExistsInList()) {
        "Corriger les contacts"
    } else {
        "Tous vos contacts sont corrects"
    }

    val schemeButtonColor = if (isSystemInDarkTheme()) AccentDarkColor else AccentColor

    val backgroundColor = if (state.isLoading) {
        schemeButtonColor.copy(alpha = 0.3f)
    } else if (viewModel.issueExistsInList()) {
        schemeButtonColor
    } else {
        schemeButtonColor.copy(alpha = 0.1f)
    }


    val borderColor = if (viewModel.issueExistsInList()) {
        Color.Transparent
    } else {
        schemeButtonColor.copy(alpha = 0.5f)
    }

    //contacts
    val contacts = if (state.onlyContactsIssues) {
        state.contacts.filter { it.hasPhoneNumberIssue() }
    } else {
        state.contacts
    }

    val appColor = if (isSystemInDarkTheme()) {
        Color.Black
    } else {
        Color.White
    }

    val appTextColor = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
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
    var callableOnWritePermissionGranted = {}
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            Log.e("contact", "Permission is granted or not: $isGranted")
            permissionState = when {
                isGranted -> {
                    callableOnWritePermissionGranted()
                    PermissionState.Granted
                }

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

    val callableToShowPermissions = {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> callableOnWritePermissionGranted()

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

                else -> {}


            }
        }
    }


    Scaffold(
        containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Mes contacts", style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )
            }, actions = {

                if (state.selectionMode) {
                    TextButton(onClick = {
                        viewModel.toggleSelectMode()
                    }) {
                        Text(
                            text = "Annuler", style = MaterialTheme.typography.bodyMedium.copy(
                                color = schemeButtonColor
                            )
                        )
                    }
                } else {
                    IconButton(onClick = {
                        showBottomSheet = true

                    }) {
                        Icon(Icons.Outlined.Info, contentDescription = "Information about app")
                    }

                    IconButton(onClick = {
                        viewModel.toggleSelectMode()
                      //  expanded = true
                    }) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = "More")
                    }
                }


                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = appColor

                    ) {
                    DropdownMenuItem(

                        onClick = {
                            // Action pour "Sélectionner"
                            viewModel.toggleSelectMode()
                            expanded = false
                        },
                        text = {
                            Text(
                                "Sélectionner",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                                )
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null
                            )
                        }, colors = MenuDefaults.itemColors(
                            leadingIconColor = appTextColor,
                            textColor = appTextColor
                        )
                    )
//                HorizontalDivider()
//                DropdownMenuItem(
//                    onClick = {
//                        // Action pour "Paramètres"
//                        expanded = false
//                    },
//                    text = {
//                        Text("Paramètres")
//                    },
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Filled.Person,
//                            contentDescription = null
//                        )
//                    }
//                )
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                titleContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
            )
            )

        }) { contentPadding ->


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)


        ) {
            Column(

            ) {

                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    FilledTextField(text = state.query, onValueChange = {
                        viewModel.onQueryChange(it)

                    }, placeHolder = "Rechercher un nom ou prénom",
                        trailingIcon = {
                            if (state.query.isNotEmpty()) {
                                Icon(Icons.Rounded.Clear, contentDescription = "Search",  tint = appTextColor.copy(0.5f), modifier = Modifier.clickable {
                                    viewModel.onQueryChange("")
                                })
//                                IconButton(onClick = { viewModel.onQueryChange("") }) {
//                                    Icon(Icons.Default.Close, contentDescription = "Search", tint = Color.Black)
//                                }
                            }
                        },
                        leadingIcon = {
                            Icon(painter = painterResource(id = R.drawable.baseline_search_24), contentDescription ="Search contacts",
                               tint = appTextColor.copy(0.5f)
                                )
                        }

                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                    FilterChip(
                        selected = !state.onlyContactsIssues, onClick = {
                            viewModel.selectAllContacts()
                        },
                        border = BorderStroke(0.dp, color = Color.Transparent),
                        label = {
                            Text(
                                text = "Tout", style = MaterialTheme.typography.bodyMedium
                            )
                        },


                        colors = FilterChipDefaults.filterChipColors(
                            containerColor =  if (isSystemInDarkTheme()) Color(0xFF1c1c20) else schemeButtonColor.copy(alpha = 0.05f),
                            selectedContainerColor = schemeButtonColor.copy(alpha = 0.8f),
                            labelColor = if (isSystemInDarkTheme()) Color.White else  Color.Black,
                            selectedLabelColor = Color.White


                        ),
                        shape = CircleShape,



                    )

                    Spacer(modifier = Modifier.width(10.dp))



                    FilterChip(
                        selected = state.onlyContactsIssues, onClick = {
                            viewModel.filterForOnlyContactsIssues()
                        },
                        border = BorderStroke(0.dp, color = Color.Transparent),
                        label = {
                            Text(
                                text = "À corriger", style = MaterialTheme.typography.bodyMedium
                            )
                        },


                        colors = FilterChipDefaults.filterChipColors(
                            containerColor =  if (isSystemInDarkTheme()) Color(0xFF1c1c20) else schemeButtonColor.copy(alpha = 0.05f),
                            selectedContainerColor = schemeButtonColor.copy(alpha = 0.8f),
                            labelColor = if (isSystemInDarkTheme()) Color.White else  Color.Black,
                            selectedLabelColor = Color.White

                        ),

                        shape = CircleShape,



                    )
                }




                if (state.isLoading && contacts.isEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    CustomProgress(modifier = Modifier.align(Alignment.CenterHorizontally))
                }  else if (contacts.isEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Aucun contacts", textAlign = TextAlign.Center ,style = MaterialTheme.typography.bodyMedium, modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally))
                }




                LazyColumn(contentPadding = PaddingValues(bottom = 80.dp, top = 20.dp)) {

                    itemsIndexed(contacts, key = { _: Int, item: Contact ->
                        "${item.id} ${viewModel.getDisplayContentOfContact(item)}"
                    }) { index, contact ->
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 30.dp),

                            color = appTextColor.copy(alpha = if (isSystemInDarkTheme()) 0.08f  else 0.05f),

                            )
                        ContactItem(contact, onRemove = {
                            viewModel.showRemoveDialog(contact)
                        }, onFitContact = {
                            callableOnWritePermissionGranted = { viewModel.fixOneContact(contact) }
                            callableToShowPermissions()

                        }, onToggleSelect = {
                            viewModel.toggleSelectionOfContact(contact)
                        }, toggleSelectionMode = state.selectionMode,
                            isSelect = viewModel.isContactSelected(contact.id)
                        )
//                        if (index < state.contacts.lastIndex) {
//                            HorizontalDivider(modifier = Modifier.padding(start = 30.dp),
//
//                                color = appTextColor.copy(alpha = 0.05f),
//
//                                )
//                        }
                    }
                }

            }

            Box(
                modifier = Modifier

                    .padding(horizontal = 10.dp)
                    .background(color = if (isSystemInDarkTheme()) Color.Black else Color.White)

                    .align(Alignment.BottomCenter)

                    .padding(bottom = 10.dp, top = 10.dp)
                //  .shadow(5.dp)


            ) {
                if (state.selectionMode) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = {
                            viewModel.removeContacts()
                        }, enabled = state.contactsSelected.isNotEmpty()) {
                            Text(
                                text = "Ignorer", style = MaterialTheme.typography.bodyLarge.copy(
                                    color = if (state.contactsSelected.isEmpty()) Color.Gray else Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }


                        Button(
                            onClick = {
                                callableOnWritePermissionGranted = { viewModel.fixSomeContacts() }
                                callableToShowPermissions()


                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.4f)
                            ), enabled = state.contactsSelected.isNotEmpty()
                        ) {
                            Text(
                                text = "Corriger", style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (state.contactsSelected.isEmpty()) Color.Gray else schemeButtonColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }


                    }
                } else {
                    CustomFilledButton(
                        text = buttonContent,
                        isEnabled = viewModel.issueExistsInList(),
                        onClick = {

                            callableOnWritePermissionGranted = { viewModel.updateContacts() }
                            callableToShowPermissions()
                        },
                        borderColor = borderColor,
                        color = backgroundColor,
                        textColor = if (viewModel.issueExistsInList()) Color.White else AccentColor,
                    )
                }


            }


        }

        if (showBottomSheet || !state.hasAlreadyGettingStarted) {

            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    viewModel.toggleGettingStarted()
                },
                sheetState = sheetState,
                containerColor = if (isSystemInDarkTheme()) Color(0xFF232428) else Color.White,
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)

            ) {
                // Sheet content
                InfosView()
            }
        }

        if (showRemoveDialogState) {
            AlertDialog(
                containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                onDismissRequest = {
                    viewModel.dismissRemoveDialog()
                },

                text = {
                    Text(text = "Êtes-vous sûr de vouloir ignorer ce contact ? Cette action est irréversible !")
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.dismissRemoveDialog()
                        viewModel.removeContact()
                    }) {
                        Text(
                            text = "Oui", style = MaterialTheme.typography.bodyMedium.copy(
                                color = AccentColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        viewModel.dismissRemoveDialog()
                    }) {
                        Text(
                            text = "Non", style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            )
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PrevHomeView() {
    HomeView()
}
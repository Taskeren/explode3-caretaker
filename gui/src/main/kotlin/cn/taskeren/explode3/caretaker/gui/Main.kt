/*
 * Copyright (c) 2023 Team Project Detonation.
 * All Rights Reserved.
 */

package cn.taskeren.explode3.caretaker.gui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.*
import cn.taskeren.explode3.caretaker.Caretaker
import kotlinx.coroutines.launch
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import javax.swing.UIManager

fun main() {
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

	// extract the library
	Caretaker.prepareDynamicLibrary()

	// call it once to make sure it's available
	Caretaker.encrypt("Hello World!".toByteArray())

	application {
		Window(
			onCloseRequest = ::exitApplication,
			title = "Caretaker",
			state = rememberWindowState(
				position = WindowPosition(alignment = Alignment.Center)
			)
		) {
			MaterialTheme {
				MainView()
			}
		}
	}
}

@Composable
@Preview
fun MainView() {

	var mode by remember { mutableStateOf(Mode.Encrypt) }

	val selectedInputFileState: MutableState<File?> = remember { mutableStateOf(null) }
	val selectedOutputFileState: MutableState<File?> = remember { mutableStateOf(null) }

	val selectedInputFile: File? by selectedInputFileState
	val selectedOutputFile: File? by selectedOutputFileState

	var signingText by remember { mutableStateOf("") }

	var isProcessing by remember { mutableStateOf(false) }

	val coroutine = rememberCoroutineScope()

	Column(
		modifier = Modifier.padding(16.dp)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			Mode.entries.forEach {
				RadioButton(
					selected = mode == it,
					onClick = { mode = it },
				)
				Text(it.nounName)
			}
		}

		if(mode == Mode.Encrypt || mode == Mode.Decrypt) {

			Row {
				FileChooseButton(selectedInputFileState) {
					OutlinedButton(
						onClick = { it.value = true }
					) {
						Text(selectedInputFile?.name ?: "Choose Input")
					}
				}
			}

			Row {
				FileChooseButton(selectedOutputFileState, mode = FileDialog.SAVE) {
					OutlinedButton(
						onClick = { it.value = true }
					) {
						Text(selectedOutputFile?.name ?: "Choose Output")
					}
				}
			}

			Row {
				OutlinedButton(
					onClick = {
						isProcessing = true
						coroutine.launch {
							try {
								val input = selectedInputFile ?: return@launch
								val output = selectedOutputFile ?: return@launch
								if(mode == Mode.Encrypt) {
									val b = Caretaker.encrypt(input.readBytes())
									output.writeBytes(b)
								}
								if(mode == Mode.Decrypt) {
									val b = Caretaker.decrypt(input.readBytes())
									output.writeBytes(b)
								}
							} catch(e: Exception) {
								println("Something went wrong when encrypting or decrypting the file.")
								println("Input: $selectedInputFile")
								println("Output: $selectedOutputFile")
								e.printStackTrace()
							} finally {
								isProcessing = false
							}
						}
					},
					colors = ButtonDefaults.buttonColors(backgroundColor = lightColors().primaryVariant)
				) {
					Text(mode.verbName)
				}

				if(isProcessing) {
					Spacer(modifier = Modifier.width(8.dp))
					Text("Processing...")
				}
			}
		}

		if(mode == Mode.Signing) {
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				OutlinedTextField(
					value = signingText,
					onValueChange = { signingText = it },
					modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5F),
					textStyle = TextStyle.Default.merge(fontFamily = FontFamily.Monospace),
					label = { Text("Signing Body") },
				)
			}

			Row {
				Text(
					text = "There's a known issue that Tab(\\t) is rendered incorrectly. This is a upstream bug, and nothing Caretaker can do to fix it.",
					color = Color.Gray,
					fontSize = 0.75F.em,
					fontStyle = FontStyle.Italic
				)
			}

			Row {
				OutlinedButton(
					onClick = {
						signingText = Caretaker.signData(signingText)
					}
				) {
					Text(mode.verbName)
				}
			}
		}
	}
}

@Composable
private fun FileChooseButton(
	file: MutableState<File?>,
	mode: Int = FileDialog.LOAD,
	content: @Composable (isVisible: MutableState<Boolean>) -> Unit
) {
	val isDialogVisibleState = remember { mutableStateOf(false) }
	var isDialogVisible by isDialogVisibleState

	content(isDialogVisibleState)

	if(isDialogVisible) {
		FileDialog(mode = mode) { result ->
			isDialogVisible = false
			file.value = result.singleOrNull()
		}
	}
}

@Composable
private fun FileDialog(
	parent: Frame? = null,
	mode: Int = FileDialog.LOAD,
	onCloseRequest: (result: Array<File>) -> Unit,
) = AwtWindow(
	create = {
		object : FileDialog(parent, "Choose a File", mode) {
			override fun setVisible(b: Boolean) {
				super.setVisible(b)
				if(!b) {
					onCloseRequest(files)
				}
			}
		}
	},
	dispose = FileDialog::dispose
)

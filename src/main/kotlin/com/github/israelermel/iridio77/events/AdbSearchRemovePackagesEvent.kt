package com.github.israelermel.iridio77.events

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.github.israelermel.iridio77.events.base.AdbSearchActionEvent
import com.github.israelermel.iridio77.receivers.IRMultilineReceiver
import com.github.israelermel.iridio77.ui.models.PackagesModel
import com.github.israelermel.iridio77.ui.models.command.Command
import com.github.israelermel.iridio77.utils.IRMessage
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class AdbSearchRemovePackagesEvent(
    val project: Project, val notification: IRNotification
) : AdbSearchActionEvent {
    override fun search(device: IDevice, command: Command): List<PackagesModel> {
        try {
            val packagesListResult = mutableListOf<PackagesModel>()

            device.executeShellCommand("pm list packages | grep \"${
                command.getCommand().lowercase()
            }.[A-Za-z0-9\\.]*\"",
                IRMultilineReceiver {
                    packagesListResult.addAll(convertReceiverListToPackageModel(it))
                }
            )

            return packagesListResult
        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_SEARCH_REMOVE_PACKAGES)
        }
        return emptyList()
    }

    override fun remove(device: IDevice, command: Command) {
        try {
            IRMessage.getMessageResourceWithDescription(MSG_SEARCH_REMOVE_PACKAGES, command.getLabel()).also {
                notification.adbNotification(it)
            }

            command.getCommand().split(";").forEach {
                device.executeShellCommand(
                    "pm uninstall --user 0 $it", NullOutputReceiver()
                )
            }

        } catch (ex: Exception) {
            notification.showAdbNotificationError(MSG_SEARCH_REMOVE_PACKAGES)
        }
    }

    private fun convertReceiverListToPackageModel(resultReceiver: Array<out String>?): MutableList<PackagesModel> {
        val packagesListResult = mutableListOf<PackagesModel>()

        resultReceiver?.forEach { item ->
            item.split(":")[1].also { singlePackage ->
                packagesListResult.add(PackagesModel(singlePackage, true))
            }
        }

        return packagesListResult
    }

    companion object {
        const val MSG_SEARCH_REMOVE_PACKAGES = "msg.adb.label.search.remove.packages"
    }
}
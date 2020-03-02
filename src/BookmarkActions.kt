package burp

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.JPopupMenu

class BookmarkActions(
    private val panel: BookmarksPanel,
    private val bookmarks: MutableList<Bookmark>,
    private val callbacks: IBurpExtenderCallbacks
) : ActionListener {
    private val table = panel.table
    private val actionsMenu = JPopupMenu()
    private val sendToRepeater = JMenuItem("Send request(s) to Repeater")
    private val sendToIntruder = JMenuItem("Send request(s) to Intruder")
    private val copyURLs = JMenuItem("Copy URL(s)")
    private val deleteMenu = JMenuItem("Delete Bookmark(s)")
    private val clearMenu = JMenuItem("Clear Bookmarks")
    private val addTag = JMenu("Add Tag")
    private val existingTags = JMenuItem("Existing Tags")
    private val newTag = JMenuItem("New Tag")

    init {
        sendToRepeater.addActionListener(this)
        sendToIntruder.addActionListener(this)
        copyURLs.addActionListener(this)
        deleteMenu.addActionListener(this)
        clearMenu.addActionListener(this)
        actionsMenu.add(sendToRepeater)
        actionsMenu.add(sendToIntruder)
        actionsMenu.add(copyURLs)
        actionsMenu.addSeparator()
        actionsMenu.add(deleteMenu)
        actionsMenu.add(clearMenu)
        actionsMenu.addSeparator()
        newTag.addActionListener(this)
        existingTags.addActionListener(this)
        addTag.add(newTag)
        addTag.add(existingTags)
        actionsMenu.add(addTag)
        panel.table.componentPopupMenu = actionsMenu
    }


    override fun actionPerformed(e: ActionEvent?) {
        if (table.selectedRow == -1) return
        val selectedBookmarks = getSelectedBookmarks()
        when (val source = e?.source) {
            deleteMenu -> {
                panel.model.removeBookmarks(selectedBookmarks)
            }
            clearMenu -> {
                panel.model.clearBookmarks()
                panel.requestViewer?.setMessage(ByteArray(0), true)
                panel.responseViewer?.setMessage(ByteArray(0), false)
            }
            copyURLs -> {
                val urls = selectedBookmarks.map { it.url }.joinToString()
                val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(StringSelection(urls), null)
            }
            else -> {
                for (selectedBookmark in selectedBookmarks) {
                    val https = useHTTPs(selectedBookmark)
                    val url = selectedBookmark.url
                    when (source) {
                        sendToRepeater -> {
                            var title = selectedBookmark.title
                            if (title.length > 10) {
                                title = title.substring(0, 9) + "+"
                            } else if (title.isBlank()) {
                                title = "[^](${bookmarks.indexOf(selectedBookmark)}"
                            }
                            callbacks.sendToRepeater(
                                url.host,
                                url.port,
                                https,
                                selectedBookmark.requestResponse.request,
                                title
                            )
                        }
                        sendToIntruder -> {
                            callbacks.sendToIntruder(
                                url.host, url.port, https,
                                selectedBookmark.requestResponse.request, null
                            )
                        }
                        newTag -> {
                            val tagToAdd = JOptionPane.showInputDialog("Tag:")
                            selectedBookmark.tags.add(tagToAdd)
                            panel.model.updateTags()
                            panel.bookmarkOptions.updateTags()
                        }
                        existingTags -> {
                            val tags = panel.model.tags.sorted().toTypedArray()
                            val tagToAdd = JOptionPane.showInputDialog(
                                null, "Select an existing tag:",
                                "Tags", JOptionPane.QUESTION_MESSAGE, null, tags, null
                            ) as String
                            selectedBookmark.tags.add(tagToAdd)
                            panel.model.updateTags()
                            panel.bookmarkOptions.updateTags()
                        }
                    }
                }
            }
        }
    }

    private fun getSelectedBookmarks(): MutableList<Bookmark> {
        val selectedBookmarks: MutableList<Bookmark> = ArrayList()
        for (index in table.selectedRows) {
            selectedBookmarks.add(bookmarks[index])
        }
        return selectedBookmarks
    }

    private fun useHTTPs(bookmark: Bookmark): Boolean {
        return (bookmark.url.protocol.toLowerCase() == "https")

    }
}

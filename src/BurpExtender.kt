@file:Suppress("unused")

package burp

class BurpExtender : IBurpExtender {
    override fun registerExtenderCallbacks(callbacks: IBurpExtenderCallbacks) {
        val tab = BookmarkTab(callbacks)
        val table = tab.bookmarkTable
        val menuItem = BookmarkMenu(table)
        callbacks.stdout.write("Bookmarks [^] v0.4.9".toByteArray())
        callbacks.stdout.write("\nAuthor: Caleb Kinney".toByteArray())
        callbacks.stdout.write("\nEmail: caleb@derail.io".toByteArray())
        callbacks.stdout.write("\nWebsite: https://derail.io".toByteArray())
        callbacks.setExtensionName("Bookmarks")
        callbacks.addSuiteTab(tab)
        callbacks.registerContextMenuFactory(menuItem)
    }
}
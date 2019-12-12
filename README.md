# Bookmarks [^]

A simple Burp Suite extension to *bookmark* requests for later, instead of those 100 unnamed repeater tabs you've got open. 

Extender requires [Burp Suite](https://portswigger.net/burp) and works in both the Community (Free) and Professional versions. 

#### Features:
* Save requests to table
* Repeat requests directly from `[^]` tab (and save to table)
* Highlight bookmarked requests in Proxy History
* Send to Repeater with tabs labeled with response titles
* Load highlighted requests from Proxy History
* Search bookmarks

## Install the Bookmarks Burp Suite Extension
### Build the extension

```sh
./gradlew fatJar
```

Extension JAR will be located at: `build/libs/bookmarks.jar`

#### Load the extension
1. Open Burp Suite
2. Go to Extender tab
3. Burp Extensions -> Add
4. Load bookmarks.jar


### Usage
#### Add bookmark
1. Select and right click on request(s)
2. Select `add bookmark(s) [^]`
3. Requests will be added to the `[^]` tab.

#### Repeat requests
1. Edit request (left)
2. Click the `Repeat Request` button
3. Response (right) will be updated

*If the `Add repeated request to table` checkbox is checked, requests will be added to the table*


## Changelog
### v0.3 - 2019-12-12
1. Import highlighted proxy history
2. Search bookmarks

### v0.2 - 2019-12-09
1. Enable the ability to repeat requests in tab

### v0.1 - 2019-12-07
1. Initial pre-release
 

# android resource loading bench

A quick and dirty too for comparing various methods of resource loading on Android.

<img width="319" height="670" alt="image" src="https://github.com/user-attachments/assets/e13ee7a1-1e94-4330-99d7-b8b8e63c06a2" />

## what's it do?

There are two buttons, each triggers a different mechanism of loading resources in the app.

* load resources - uses the
  [ResourcesLoaderBench](https://github.com/breedx-splk/android_resource_loading_bench/blob/main/app/src/main/java/com/splunk/testo/ResourcesLoaderBench.kt)
  to load 500 properties files in a tight loop. Each properties file is read into a stream, parsed
  as a Properties object, and the value is verified.
* load classes - uses the 
  [ClassesLoaderBench](https://github.com/breedx-splk/android_resource_loading_bench/blob/main/app/src/main/java/com/splunk/testo/ClassesLoaderBench.kt)
  to load 500 classes in a tight loop. It uses `Class.forName(...)` to load the class and then 
  uses reflection to read the `VERSION` field. This mirrors what is done by the java instrumentation.

## results

I haven't done an exhaustive analysis. Feel free to submit your own results...but here's some
basic raw data:

* Emulator on a macbook - Pixel 7
  * resources: ~40ms
  * classes: ~4ms
* Real Pixel 7
  * resources: ~76ms
  * classes: ~11ms


## warnings/caveats

* This is a very basic/stupid benchmark. There are tons of factors that have not been accounted for.
* Caching: Each button should only be used once per application load. The data will be aggressively
  cached by the system, so subsequent clicks will be faster. The app should be closed between
  attempts.
* Speculation: it's possible that the parsing and creation of Properties instances is more expensive
  than necessary. Simply reading a text value instead of a property value might be faster. 
  Feel free to test this theory.

# android resource loading bench

A quick and dirty tool for comparing various methods of resource loading on Android.

<img width="314" height="670" alt="image" src="https://github.com/user-attachments/assets/9308a6cc-4dbe-468c-8b96-27f62c77a90c" />

## what's it do?

There are two buttons, each triggers a different mechanism of loading resources in the app.

* load resources - uses the
  [ResourcesLoaderBench](https://github.com/breedx-splk/android_resource_loading_bench/blob/main/app/src/main/java/com/splunk/testo/ResourcesLoaderBench.kt)
  to load 500 properties files in a tight loop. Each properties file is read into a stream, parsed
  as a Properties object, and the value is verified. This mirrors the 
  [existing behavior of the java instrumentation agent](https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/main/instrumentation-api/src/main/java/io/opentelemetry/instrumentation/api/internal/EmbeddedInstrumentationProperties.java#L66).
* load classes - uses the 
  [ClassesLoaderBench](https://github.com/breedx-splk/android_resource_loading_bench/blob/main/app/src/main/java/com/splunk/testo/ClassesLoaderBench.kt)
  to load 500 classes in a tight loop. It uses `Class.forName(...)` to load the class and then 
  uses reflection to read the `VERSION` field. 
  This mirrors the [approach taken in PR 18600](https://github.com/open-telemetry/opentelemetry-java-instrumentation/pull/18600/changes#diff-4a2bc43880e9bc7f66e483536305574d3323469a681e43e85eb05b7e478f2346R86).

## results

I haven't done an exhaustive analysis. Feel free to submit your own results...but here's some
basic raw data:

* Emulator on a macbook - Pixel 7
  * resources: ~40ms
  * classes: ~4ms
* Real Pixel 7
  * resources: ~92ms
  * classes: ~11ms

Loading classes is consistently faster, by nearly an order of magnitude. 
See caveats about this below...

## warnings/caveats

* This is a very basic/stupid benchmark. There are tons of factors that have not been accounted for.
* Environmental factors will impact the results. 
* Caching: Each button should only be used once per application load. The data will be aggressively
  cached by the system, so subsequent clicks will be faster. The app should be fully restarted between
  attempts.
* Speculation: it's possible that the parsing and creation of Properties instances is more expensive
  than necessary. Simply reading a text value instead of a property value might be faster. 
  Feel free to test this theory.
* Might be interesting to try with 10x the files? I'm not convinced the number files impacts
  the loading all that much...so that 1 file or 100 files are even close to the same time?

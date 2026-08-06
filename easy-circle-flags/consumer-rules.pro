# ProGuard/R8 rules shipped inside the AAR and applied to every consuming app.
#
# This library needs no keep rules of its own: nothing in it is reached by
# reflection, by name from XML, or from native code, so R8 can shrink and rename
# it freely. Verified by building a minified release APK that calls CircleFlag and
# confirming the library survives in the output.
#
# The rules its dependencies need - Coil's ServiceLoader-registered decoder and
# fetcher entries, OkHttp's and Okio's platform lookups - travel with those
# artifacts' own consumer rules, so they must not be duplicated here.
#
# Keep anything added below to a minimum: a rule here applies to every consuming
# app and cannot be removed by an app that does not want it.

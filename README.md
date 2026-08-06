
# EasyCircleFlags

A lightweight Jetpack Compose library for displaying circle country flags using ISO 3166-1 alpha-2 codes. Coil 3 and SVG.

---

## Installation

### 1. Add the JitPack repository

In your project’s `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

### 2. Add the dependency

**Using a version catalog (recommended)**

In `gradle/libs.versions.toml`:

```toml
[versions]
easyCircleFlags = "1.1.0"

[libraries]
easy-circle-flags = { module = "com.github.sam-a1a:EasyCircleFlags_Android_Kotlin", version.ref = "easyCircleFlags" }
```

In your module’s `build.gradle.kts`:

```kotlin
dependencies {
    implementation(libs.easy.circle.flags)
}
```

**Without a version catalog**

```kotlin
dependencies {
    implementation("com.github.sam-a1a:EasyCircleFlags_Android_Kotlin:1.1.0")
}
```

---

## Quick Start

```kotlin
import com.sam.easycircleflags.CircleFlag

CircleFlag(countryCode = "us")
```

All flags are loaded and cached automatically by Coil 3.  
You can use the composable inside `LazyColumn`, `Row`, or any Composable layout.

```kotlin
LazyColumn {
    items(countries) { country ->
        Row {
            CircleFlag(
                countryCode = country.code,
                size = 32.dp
            )
            Text(country.name)
        }
    }
}
```

---

## Customization

### Placeholder and error states

You can override the default grey flag placeholder with any `Painter` or `Color`.

```kotlin
CircleFlag(
    countryCode = "de",
    placeholderColor = Color.LightGray,
    errorPainter = painterResource(R.drawable.ic_broken_image)
)
```

### Priority rules

| Situation | Result |
|---|---|
| Both `placeholderPainter` and `placeholderColor` provided | `placeholderPainter` is used |
| Only `placeholderColor` provided | Solid color, drawn as a circle to match the flags |
| Neither provided | Built‑in flag placeholder vector |
| Same logic applies to `errorPainter` / `errorColor` |

---

## API Reference

```kotlin
@Composable
fun CircleFlag(
    countryCode: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Flag of $countryCode",
    size: Dp = 48.dp,
    contentScale: ContentScale = ContentScale.Fit,
    placeholderPainter: Painter? = null,
    placeholderColor: Color? = null,
    errorPainter: Painter? = null,
    errorColor: Color? = null,
    imageLoader: ImageLoader = CircleFlagImageLoader.get(LocalContext.current),
)
```

| Parameter | Type | Default | Description |
|---|---|---|---|
| `countryCode` | `String` | (required) | ISO 3166‑1 alpha‑2 code (case‑insensitive). Example: `"us"`, `"de"`, `"fr"`. |
| `modifier` | `Modifier` | `Modifier` | Standard Compose modifier for the flag image. |
| `contentDescription` | `String?` | `"Flag of $countryCode"` | Accessibility description. |
| `size` | `Dp` | `48.dp` | Width and height of the composable. |
| `contentScale` | `ContentScale` | `ContentScale.Fit` | How the flag is scaled within the given size. |
| `placeholderPainter` | `Painter?` | `null` | Custom painter shown while the flag loads. Overrides `placeholderColor`. |
| `placeholderColor` | `Color?` | `null` | Solid color shown while the flag loads. |
| `errorPainter` | `Painter?` | `null` | Custom painter shown when loading fails. Overrides `errorColor`. |
| `errorColor` | `Color?` | `null` | Solid color shown when loading fails. |
| `imageLoader` | `ImageLoader` | shared flag loader | The Coil loader to use. Pass your own if the app already has one configured. |

---

## How It Works

1. The library constructs a URL:  
   `https://hatscripts.github.io/circle-flags/flags/{countryCode}.svg`
2. [Coil 3](https://github.com/coil-kt/coil) handles network fetching, SVG decoding, and caching.
3. The flag appears inside a standard Compose `AsyncImage`.

No additional configuration is needed. The library declares the `INTERNET`
permission itself, so there is nothing to add to your manifest.

### One loader for the whole process

Every `CircleFlag` shares a single `ImageLoader`, created on first use and held for the
life of the process. That matters in a list: an `ImageLoader` owns a memory cache sized
as a share of the app heap, and the OkHttp client under it owns a connection pool and a
thread pool, so a per-flag loader would multiply all of it by the number of flags on
screen.

Flags are also decoded at the size they are drawn rather than the 512x512 the SVGs
declare - roughly 80 KB in memory for a 48dp flag instead of about 1 MB.

If your app already configures Coil, hand that loader in via `imageLoader` (or install
this one as Coil's singleton, below) so everything shares one cache.

```kotlin
class MyApp : Application(), SingletonImageLoader.Factory {
    override fun newImageLoader(context: PlatformContext): ImageLoader =
        CircleFlagImageLoaderFactory.newImageLoader(context)
}
```

### Country codes

Codes are case-insensitive and validated before they reach the network. Beyond the
alpha-2 codes, anything the flag set ships works, including subdivisions (`"gb-eng"`),
underscored names (`"european_union"`) and the language flags (`"language/ar"`).

A code that is not a usable flag name renders the error painter rather than throwing, so
a bad value from a server response cannot take down the screen. To resolve URLs
yourself:

```kotlin
CircleFlagUrls.getFlagUrl("us")        // throws IllegalArgumentException if unusable
CircleFlagUrls.getFlagUrlOrNull("us")  // null if unusable
```

---

## License

```
MIT License

Copyright (c) 2025 Sam

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## Contributing

Pull requests are welcome. If you encounter a missing or broken flag, please open an issue with the country code and a brief description.

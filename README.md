```markdown
# EasyCircleFlags

A Jetpack Compose library that displays circle flags from the
[Circle Flags](https://hatscripts.github.io/circle-flags/) collection.
Provide a two‑letter country code and the library loads the corresponding SVG flag automatically.

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
easyCircleFlags = "1.0.0"

[libraries]
easy-circle-flags = { module = "com.github.sam-a1a:easy-circle-flags", version.ref = "easyCircleFlags" }
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
    implementation("com.github.sam-a1a:easy-circle-flags:1.0.0")
}
```

---

## Quick Start

```kotlin
import com.sam.easycircleflags.CircleFlag

CircleFlag(countryCode = "us")
```

All flags are loaded and cached automatically by Coil.  
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
| Only `placeholderColor` provided | Solid color placeholder |
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

---

## How It Works

1. The library constructs a URL:  
   `https://hatscripts.github.io/circle-flags/flags/{countryCode}.svg`
2. [Coil](https://github.com/coil-kt/coil) handles image loading, SVG decoding, and caching.
3. The flag appears inside a standard Compose `AsyncImage`.

No additional configuration is needed.

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

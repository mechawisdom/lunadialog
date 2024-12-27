
![MixCollage-27-Dec-2024-07-16-PM-2413](https://github.com/user-attachments/assets/d29db299-883e-44f6-b93c-ef721a949dae)


# LunaDialog (ENGLISH)


LunaDialog is a simple and elegant dialog box library for Android.

## Usage

### Step 1: Add the JitPack repository to your build file

Add it in your root `build.gradle` file at the end of the repositories section:

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2: Add the dependency

Add the following dependency to your `build.gradle` file:

```
dependencies {
    // Kotlin DSL 
    implementation("com.github.mechawisdom:lunadialog:1.0.0")
    // Groovy DSL
    implementation 'com.github.mechawisdom:lunadialog:1.0.0'
}
```

### Step 3: Usage

```
class MainActivity : AppCompatActivity() {
    private lateinit var lunaProgressDialog: LunaProgressDialog
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lunaProgressDialog = LunaProgressDialog.Builder(this)
            .setTitleText("Title")
            .setDescriptionText("This is a desc")
            .setCancelableOption(true)
            .setContainerOrientation(OrientationType.VERTICAL)
            .setAnimationType(AnimationType.IMAGEVIEW)
            .setAnimationDelay(200L)
            .setAnimationStyle(AnimationStyle.ZOOM_NORMAL)
            .setAnimationStart(true)
            .setProgressDrawable(ProgressDrawable.DOT_PROGRESS)
            .setProgressScaleType(ImageView.ScaleType.CENTER_CROP)
            .setTitleTextColor(Color.GREEN)
            .setTitleTextStyle(Typeface.BOLD)
            .setTitleFontFamily(R.font.beach)
            .setDescriptionFontFamily(R.font.poppins_medium)
            .setDescriptionTextColor(Color.RED)
            .setAnimationFPS(18)
            .setProgressImageSize(64, 64)
            .setTitleTextSize(14)
            .setTextContainerMargin(0, 0, 0, 0)
            .setTextContainerGravity(Gravity.CENTER)
            .setContainerPositionGravity(Gravity.CENTER, 0, 0)
            .setContainerBackgroundShape(GradientDrawable.RECTANGLE)
            .setContainerDimAmount(0.1f)
            .setContainerCornerRadius(16)
            .setContainerPadding(12)
            .setContainerBackgroundColor(ContainerColor.BLACK.COLOR(applicationContext))
            .build()


        binding.showDialogButton.setOnClickListener {
            if (lunaProgressDialog.isShowing) {
                lunaProgressDialog.dismiss()
            } else {
                lunaProgressDialog.show()
            }
        }
    }
}
```
# LunaDialog (TÜRKÇE)

LunaDialog, Android için basit ve şık bir diyalog kutusu kütüphanesidir.

## Kullanım

### Adım 1: JitPack deposunu projenize ekleyin

Projenizin kök `build.gradle` dosyasına aşağıdaki JitPack reposunu ekleyin:

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Adım 2: Bağımlılığı ekleyin

Projenizin `build.gradle` dosyasına aşağıdaki bağımlılığı ekleyin:

```
dependencies {
    // Kotlin DSL 
    implementation("com.github.mechawisdom:lunadialog:1.0.0")
    // Groovy DSL
    implementation 'com.github.mechawisdom:lunadialog:1.0.0'
}
```

### Adım 3: Kullanım


```
class MainActivity : AppCompatActivity() {
    private lateinit var lunaProgressDialog: LunaProgressDialog
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lunaProgressDialog = LunaProgressDialog.Builder(this)
            .setTitleText("Title")
            .setDescriptionText("This is a desc")
            .setCancelableOption(true)
            .setContainerOrientation(OrientationType.VERTICAL)
            .setAnimationType(AnimationType.IMAGEVIEW)
            .setAnimationDelay(200L)
            .setAnimationStyle(AnimationStyle.ZOOM_NORMAL)
            .setAnimationStart(true)
            .setProgressDrawable(ProgressDrawable.DOT_PROGRESS)
            .setProgressScaleType(ImageView.ScaleType.CENTER_CROP)
            .setTitleTextColor(Color.GREEN)
            .setTitleTextStyle(Typeface.BOLD)
            .setTitleFontFamily(R.font.beach)
            .setDescriptionFontFamily(R.font.poppins_medium)
            .setDescriptionTextColor(Color.RED)
            .setAnimationFPS(18)
            .setProgressImageSize(64, 64)
            .setTitleTextSize(14)
            .setTextContainerMargin(0, 0, 0, 0)
            .setTextContainerGravity(Gravity.CENTER)
            .setContainerPositionGravity(Gravity.CENTER, 0, 0)
            .setContainerBackgroundShape(GradientDrawable.RECTANGLE)
            .setContainerDimAmount(0.1f)
            .setContainerCornerRadius(16)
            .setContainerPadding(12)
            .setContainerBackgroundColor(ContainerColor.BLACK.COLOR(applicationContext))
            .build()


        binding.showDialogButton.setOnClickListener {
            if (lunaProgressDialog.isShowing) {
                lunaProgressDialog.dismiss()
            } else {
                lunaProgressDialog.show()
            }
        }
    }
}
```
---

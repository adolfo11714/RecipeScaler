## RecipeScaler

Simple Java Swing app for creating, editing, scaling, exporting, and printing recipes.

## Run On Another Machine (After Unzipping)

### 1) Install Java (JDK 17 or newer)

- Install a JDK (not just a JRE), version `17+`.
- Verify installation:

```powershell
java -version
javac -version
```

Both commands should work before continuing.

### 2) Unzip and open the project

- Unzip the folder anywhere, for example:
  - `C:\Projects\RecipeScaler`
- Open a terminal in the unzipped project root (the folder that contains `src`).

### 3) Compile the project

From the project root, run:

```powershell
mkdir build
$files = Get-ChildItem -Path .\src -Recurse -Filter *.java | ForEach-Object FullName
javac --release 17 -encoding UTF-8 -d build -sourcepath src $files
```

If there are no errors, compilation succeeded.

### 4) Run the app

```powershell
java -cp build com.adolfomartinez.recipescaler.App
```

The RecipeScaler window should open.

## What folders are created/used at runtime

- `saved-recipes`: default location for created recipes.
- `saved-scaled-recipes`: used when saving scaled recipes.

If these folders do not exist yet, the app creates them when needed.

## Optional: Run from VS Code

1. Install Extension Pack for Java.
2. Open the unzipped folder in VS Code.
3. Open `src/com/adolfomartinez/recipescaler/App.java`.
4. Click **Run** above `main`.

## Troubleshooting

- **`java` or `javac` not found**
  - Reinstall JDK 17+ and restart terminal.
- **Main menu buttons are disabled**
  - Add at least one `.txt` recipe in `saved-recipes` using the **Create Recipe** button.
- **Printing/email actions do not open**
  - Ensure the OS has a default printer/mail client configured.

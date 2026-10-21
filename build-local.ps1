param([string]$Sdk = "$env:LOCALAPPDATA\Android\Sdk", [string]$Jdk = "$env:LOCALAPPDATA\Programs\Eclipse Adoptium\jdk-25.0.2.10-hotspot")
$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
$env:JAVA_HOME = $Jdk
$env:Path = "$Jdk\bin;$env:Path"
function Run($Executable, $Arguments) {
    & $Executable @Arguments
    if ($LASTEXITCODE -ne 0) { throw "$Executable failed with exit $LASTEXITCODE" }
}
$build = Join-Path $PSScriptRoot ('build\local-' + [DateTime]::Now.ToString('yyyyMMddHHmmss'))
New-Item -ItemType Directory -Force -Path "$build\classes", "$build\dex", "$build\checks" | Out-Null
$jar = Join-Path $Sdk 'platforms\android-36\android.jar'
$bt = Join-Path $Sdk 'build-tools\36.0.0'
$source = 'app\src\main\java\com\hlayisa\pantrywise'
Run "$Jdk\bin\javac.exe" (@('--release','8','-d',"$build\checks") + @("$source\Stock.java","$source\Meal.java","$source\Matcher.java",'tests\MatcherChecks.java'))
Run "$Jdk\bin\java.exe" @('-cp',"$build\checks",'com.hlayisa.pantrywise.MatcherChecks')
$files = @(Get-ChildItem -LiteralPath $source -Filter '*.java' | ForEach-Object FullName)
Run "$Jdk\bin\javac.exe" (@('--release','8','-classpath',$jar,'-d',"$build\classes") + $files)
[xml]$manifest = Get-Content -Raw 'app\src\main\AndroidManifest.xml'
$manifest.DocumentElement.SetAttribute('package','com.hlayisa.pantrywise')
$manifest.Save("$build\AndroidManifest.xml")
Run "$bt\aapt2.exe" @('link','-I',$jar,'--manifest',"$build\AndroidManifest.xml",'--debug-mode','--min-sdk-version','23','--target-sdk-version','36','--version-code','1','--version-name','1.0','-o',"$build\unsigned.apk")
$classes = @(Get-ChildItem -LiteralPath "$build\classes" -Recurse -Filter '*.class' | ForEach-Object FullName)
Run "$bt\d8.bat" (@('--lib',$jar,'--min-api','23','--output',"$build\dex") + $classes)
Run "$Jdk\bin\jar.exe" @('uf',"$build\unsigned.apk",'-C',"$build\dex",'classes.dex')
Run "$bt\zipalign.exe" @('-f','4',"$build\unsigned.apk","$build\aligned.apk")
$key = Join-Path $PSScriptRoot 'build\debug.keystore'
if (!(Test-Path -LiteralPath $key)) {
    Run "$Jdk\bin\keytool.exe" @('-genkeypair','-keystore',$key,'-storepass','android','-keypass','android','-alias','androiddebugkey','-keyalg','RSA','-keysize','2048','-validity','10000','-dname','CN=Android Debug,O=Android,C=US','-storetype','JKS')
}
Run "$bt\apksigner.bat" @('sign','--ks',$key,'--ks-pass','pass:android','--key-pass','pass:android','--out',"$PSScriptRoot\PantryWise-debug.apk","$build\aligned.apk")
Run "$bt\apksigner.bat" @('verify',"$PSScriptRoot\PantryWise-debug.apk")
Write-Output 'PASS: APK compiled, aligned and signature verified.'

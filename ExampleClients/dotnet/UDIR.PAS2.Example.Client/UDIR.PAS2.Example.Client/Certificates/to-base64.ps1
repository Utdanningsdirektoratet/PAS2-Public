<#
	Leser rå bytes fra angitt fil, og sender BASE64-encoded versjon til STDOUT
#>
param(
	[ValidateScript({ test-path -Path $_ -PathType Leaf})]
	[Parameter(Mandatory=$true)]
	$infile
)
$path =  resolve-path $infile | select -ExpandProperty Path
# Read the entire file to an array of bytes.
$bytes = [System.IO.File]::ReadAllBytes($path)
[System.Convert]::ToBase64String($bytes)


XSD schemas can be downloaded from {url to testenvironment}/sas/skjema.
The ZIP file returned from this resource includes xsd files for integrating with PAS.

To generate classes for this XSD use the following command from Visual Studio Developer command prompt

xsd /c <the xsd files you want to generate classes for>

In this project the result of running this command has been stored in GeneratedClasses.cs
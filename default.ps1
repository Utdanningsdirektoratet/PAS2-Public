
properties {
    $base_dir = resolve-path .
    $src_dir = "$base_dir\ExampleClients\dotnet\UDIR.PAS2.Example.Client\";
    $packages_dir = "$src_dir\packages";
    $config = 'debug';
	$sln = "$src_dir\UDIR.PAS2.Example.Client.sln";
    $runsOnBuildServer = $false;
    $dist_dir = "$base_dir\dist";
    $ant_buildfile = "$base_dir\ExampleClients\java\eksamen\build.xml"
}

Import-module "$psscriptroot\tools\teamcity.psm1" -WarningAction SilentlyContinue

FormatTaskName {
    param($taskName)
    write-host "Executing Task: $taskName" -foregroundcolor Magenta
}

task -name validate-config -depends add-teamcity-reporting -action {
    assert ( 'debug', 'release' -contains $config) "Invalid config: $config. Should be 'debug' or 'release'"
    Write-host "Build version is $build_version"
    Write-host "Configuration is $config"
    Write-host "Running on build server: $runsOnBuildServer"
    if (-not(test-path -pathtype container -path $dist_dir)) { md $dist_dir | out-null }
}

task -name add-teamcity-reporting -precondition { return $runsOnBuildServer } -action {
    exec {
        TaskSetup {
            $taskName = $psake.context.Peek().currentTaskName
            $global:pasPakkerBuildResult = "##teamcity[buildStatus status='FAILURE' text='Build failed on step $taskName']"
            TeamCity-ReportBuildProgress "Running task $taskName"
        }
    }
}


task -name rebuild -depends clean, build

task -name ensure-nuget -action {
    exec {
        nuget update -self
    }
}

task -name ensure-ant -action {
    exec {
        get-command ant -ErrorAction SilentlyContinue | Out-Null
        if (!$?) {
            (get-psprovider 'FileSystem').Home = $(pwd)
            scoop install ant
        }
    }
}

task -name compile_java -depends ensure-ant -action {
    exec {
        ant -f $ant_buildfile
    }
}

task -name restore-nuget -depends ensure-nuget  -action {
	exec {
		nuget restore $sln
	}
}

task -name build-sln -depends validate-config, restore-nuget -action {
    exec {
        run-msbuild $sln 'build' $config
    }
}

task -name clean -depends validate-config -action {
    exec {
        run-msbuild $sln 'clean' $config
    }
}


task -name ci -depends compile_java,build-sln -action {
    exec {
        
    }
}

task build -depends build-sln
task default -depends build-sln

########### Helper functions ########################

function run-msbuild($sln_file, $t, $cfg) {
    $v = if ($runsOnBuildServer) { 'n'} else { 'q' }
    Framework '4.6.1'
    msbuild /nologo /verbosity:$v $sln_file /t:$t /p:Configuration=$cfg /tv:14.0
}
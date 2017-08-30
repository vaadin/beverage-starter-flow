#!/usr/bin/env bash

# TRAVIS_PULL_REQUEST == "false" for a normal branch commit, the PR number for a PR
# TRAVIS_BRANCH == target of normal commit or target of PR
# TRAVIS_SECURE_ENV_VARS == true if encrypted variables, e.g. SONAR_HOST is available
# TRAVIS_REPO_SLUG == the repository, e.g. vaadin/vaadin
# SKIP_SONAR == skip sonar checks

# Exclude the bower_components and node_modules from Sonar analysis
SONAR_EXCLUSIONS=**/bower_components/**,**/node_modules/**,**/node/**

function getSonarDetails {
    if [ "$TRAVIS_PULL_REQUEST" != "false" ]
    then
        echo "-Dsonar.github.repository=$TRAVIS_REPO_SLUG \
               -Dsonar.github.oauth=$SONAR_GITHUB_OAUTH \
               -Dsonar.github.pullRequest=$TRAVIS_PULL_REQUEST \
               -Dsonar.analysis.mode=issues"
    else
        echo "-Dsonar.analysis.mode=publish"
    fi
}

function runSonar {
    if [ "$SKIP_SONAR" != "true" ]
    then
        # Sonar should be run after the project is built so that findbugs can analyze compiled sources
        echo "Running Sonar"
        mvn -B -e -V \
            -Dmaven.javadoc.skip=false \
            -Dmaven.repo.local=$TRAVIS_BUILD_DIR/localrepo \
            -Dsonar.exclusions=$SONAR_EXCLUSIONS \
            -Dsonar.verbose=true \
            -Dsonar.host.url=$SONAR_HOST \
            -Dsonar.login=$SONAR_LOGIN \
            $(getSonarDetails) \
            -DskipTests \
            compile sonar:sonar
    else
        echo "SKIP_SONAR env variable is set to 'true', skipping sonar."
    fi
}

mvn -B -e -V clean verify

# Get the status for the previous maven command and if not exception then run sonar.
STATUS=$?
if [ $STATUS -eq 0 ]
then
    runSonar
else
    echo "Build failed, skipping sonar."
    exit 1
fi

#!groovy

import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()
println "Creating local admin"

def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount('admin', '{{ admin_pass }}')
instance.setSecurityRealm(hudsonRealm)


def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStartegy(strategy)
instance.save()

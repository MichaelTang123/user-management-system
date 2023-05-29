package com.michaeltang.usermanagement.test.bdd.stepdefs;

import org.junit.runner.RunWith;

import io.cucumber.java8.En;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/resources/features",
		plugin = {"pretty", "html:target/cucumber-reports.html"})
public class TestRunner implements En {

}

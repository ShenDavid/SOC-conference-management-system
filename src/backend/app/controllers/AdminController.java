package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import play.mvc.Http.Session;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

public class AdminController extends Controller {

    private FormFactory formFactory;

    @Inject
    public AdminController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result getRole(String conf_url, String username)
    {
        Conference tmp = new Conference();
        String conf = conf_url.replace("%20"," ");
        int res = tmp.getrole(username, conf);
        return ok(Integer.toString(res));
    }

}
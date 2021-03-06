/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package oracle.db.microservices;


import java.sql.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/")
@ApplicationScoped
public class ATPAQAdminResource {
  PropagationSetup propagationSetup = new PropagationSetup();

  @Inject
  @Named("orderpdb")
  private DataSource orderpdbDataSource; // .setFastConnectionFailoverEnabled(false) to get rid of SEVERE yet benign message

  @Inject
  @Named("inventorypdb")
  private DataSource inventorypdbDataSource;

  @Path("/testdatasources")
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String testdatasources() {
      System.out.println("test datasources...");
      try {
          System.out.println("ATPAQAdminResource.testdatasources inventorypdbDataSource connection:" + inventorypdbDataSource.getConnection());
      } catch (Exception e) {
          e.printStackTrace();
      }
      try {
          System.out.println("ATPAQAdminResource.testdatasources orderpdbDataSource connection:" + orderpdbDataSource.getConnection());
      } catch (Exception e) {
          e.printStackTrace();
      }
      return "success";
  }

  @Path("/setupAll")
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String setupAll() {
    String returnValue = "";
    try {
      System.out.println("setupAll ...");
      returnValue += propagationSetup.createUsers(orderpdbDataSource, inventorypdbDataSource);
      returnValue += propagationSetup.createInventoryTable(inventorypdbDataSource);
      returnValue += propagationSetup.createDBLinks(orderpdbDataSource, inventorypdbDataSource);
      returnValue += propagationSetup.setup(orderpdbDataSource, inventorypdbDataSource,
              true, true);
      return " result of setupAll : success... " + returnValue;
    } catch (Exception e) {
      e.printStackTrace();
      returnValue += e;
      return " result of setupAll : " + returnValue;
    }
  }

  @Path("/createUsers")
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String createUsers() {
    String returnValue = "";
    try {
      System.out.println("createUsers ...");
      returnValue += propagationSetup.createUsers(orderpdbDataSource, inventorypdbDataSource);
      return " result of createUsers : success... " + returnValue;
    } catch (Exception e) {
      e.printStackTrace();
      returnValue += e;
      return " result of createUsers : " + returnValue;
    }
  }

  @Path("/createInventoryTable")
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String createInventoryTable() {
    String returnValue = "";
    try {
      System.out.println("createInventoryTable ...");
      returnValue += propagationSetup.createInventoryTable(inventorypdbDataSource);
      return " result of createInventoryTable : success... " + returnValue;
    } catch (Exception e) {
      e.printStackTrace();
      returnValue += e;
      return " result of createInventoryTable : " + returnValue;
    }
  }

  @Path("/createDBLinks")
  @GET
  @Produces(MediaType.TEXT_HTML) // does verifyDBLinks as well
  public String createDBLinks() {
    String returnValue = "";
    try {
      System.out.println("createDBLinks ...");
      returnValue += propagationSetup.createDBLinks(orderpdbDataSource, inventorypdbDataSource);
      return " result of createDBLinks : success... " + returnValue;
    } catch (Exception e) {
      e.printStackTrace();
      returnValue += e;
      return " result of createDBLinks : " + returnValue;
    }
  }

  @Path("/verifyDBLinks")
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String verifyDBLinks() {
    String returnValue = "";
    try {
      System.out.println("verifyDBLinks ...");
      returnValue += propagationSetup.verifyDBLinks(orderpdbDataSource, inventorypdbDataSource, "verifyDBLinks");
      return " result of verifyDBLinks : success... " + returnValue;
    } catch (Exception e) {
      e.printStackTrace();
      returnValue += e;
      return " result of verifyDBLinks : " + returnValue;
    }
  }

  @Path("/setupTablesQueuesAndPropagation")
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String setupTablesQueuesAndPropagation() {
    String returnValue = "";
    try {
      System.out.println("setupTablesQueuesAndPropagation ...");
      returnValue += propagationSetup.setup(orderpdbDataSource, inventorypdbDataSource,
              true, true);
      return " result of setupTablesQueuesAndPropagation : success... " + returnValue;
    } catch (Exception e) {
      e.printStackTrace();
      returnValue += e;
      return " result of setupTablesQueuesAndPropagation : " + returnValue;
    }
  }


  @Path("/setupOrderToInventory")
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String setupOrderToInventory() {
    String returnValue = "";
    return getString(returnValue, "setupOrderToInventory ...", true, false,
            " result of setupOrderToInventory : success... ", " result of setupOrderToInventory : ");
  }

  @Path("/setupInventoryToOrder")
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String setupInventoryToOrder() {
    String returnValue = "";
    return getString(returnValue, "setupInventoryToOrder ...", false, true,
            " result of setupInventoryToOrder : success... ", " result of setupInventoryToOrder : ");
  }

  private String getString(String returnValue, String s, boolean b, boolean b2, String s2, String s3) {
    try {
      System.out.println(s);
      returnValue += propagationSetup.setup(orderpdbDataSource, inventorypdbDataSource,
              b, b2);
      return s2 + returnValue;
    } catch (Exception e) {
      e.printStackTrace();
      returnValue += e;
      return s3 + returnValue;
    }
  }

  @Path("/testInventoryToOrder")
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String testInventoryToOrder() {
    String returnValue = "";
    try {
      System.out.println("testInventoryToOrder ...");
      returnValue += propagationSetup.testInventoryToOrder(orderpdbDataSource, inventorypdbDataSource);
      return " result of testInventoryToOrder : success... " + returnValue;
    } catch (Exception e) {
      e.printStackTrace();
      returnValue += e;
      return " result of testInventoryToOrder : " + returnValue;
    }
  }

  @Path("/testOrderToInventory")
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String testOrderToInventory() {
    String returnValue = "";
    try {
      System.out.println("testOrderToInventory ...");
      returnValue += propagationSetup.testOrderToInventory(orderpdbDataSource, inventorypdbDataSource);
      return " result of testOrderToInventory : success... " + returnValue;
    } catch (Exception e) {
      e.printStackTrace();
      returnValue += e;
      return " result of testOrderToInventory : " + returnValue;
    }
  }

  @Path("/execute")
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String execute(@QueryParam("pdb") String pdb, @QueryParam("sql") String sql, @QueryParam("user") String user, @QueryParam("password") String password) {
    try {
      System.out.println("pdb:"+pdb+" execute sql = [" + sql + "], user = [" + user + "]");
      boolean isUserPWPresent = user != null && password != null && !user.equals("") && !password.equals("");
      System.out.println("pdb:"+pdb+" execute sql = [" + sql + "], user = [" + user + "] isUserPWPresent:" + isUserPWPresent);
      DataSource dataSource = "order".endsWith(pdb) ?orderpdbDataSource:inventorypdbDataSource;
      Connection connection = isUserPWPresent ?  dataSource.getConnection(user, password): dataSource.getConnection();
      System.out.println("connection:" + connection);
      connection.createStatement().execute(sql);
      return " result of sql = [" + sql + "], user = [" + user + "]" + " : " + "success";
    } catch (Exception e) {
      e.printStackTrace();
      return " result of sql = [" + sql + "], user = [" + user + "]" + " : " + e;
    }
  }


  @Path("/unschedulePropagation")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response unschedulePropagation() throws SQLException {
    final Response returnValue = Response.ok()
            .entity("Connection obtained successfully metadata:" + propagationSetup.unscheduleOrderToInventoryPropagation(orderpdbDataSource))
            .build();
    return returnValue;
  }


  @Path("/getConnectionMetaData")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response getConnectionMetaData() throws SQLException {
    final Response returnValue = Response.ok()
            .entity("Connection obtained successfully metadata:" + orderpdbDataSource.getConnection().getMetaData())
            .build();
    return returnValue;
  }


}

/**
 * Azure Active Directory Authentication Plugin for SonarQube
 * <p>
 * Copyright (c) 2016 Microsoft Corporation
 * All rights reserved.
 * <p>
 * The MIT License (MIT)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.almrangers.auth.aad;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.sonar.api.config.Configuration;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.server.ServerSide;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.sonar.api.PropertyType.*;

@ServerSide
public class AadSettings {
  protected static final String CLIENT_ID = "sonar.auth.aad.clientId.secured";
  protected static final String CLIENT_SECRET = "sonar.auth.aad.clientSecret.secured";
  protected static final String ENABLED = "sonar.auth.aad.enabled";
  protected static final String ALLOW_USERS_TO_SIGN_UP = "sonar.auth.aad.allowUsersToSignUp";
  protected static final String TENANT_ID = "sonar.auth.aad.tenantId";
  protected static final String DIRECTORY_LOCATION = "sonar.auth.aad.directoryLocation";
  protected static final String DIRECTORY_LOC_GLOBAL = "Azure AD (Global)";
  protected static final String DIRECTORY_LOC_USGOV = "Azure AD for US Government";
  protected static final String DIRECTORY_LOC_DE = "Azure AD for Germany";
  protected static final String DIRECTORY_LOC_CN = "Azure AD China";
  protected static final String ENABLE_GROUPS_SYNC = "sonar.auth.aad.enableGroupsSync";
  protected static final String LOGIN_STRATEGY = "sonar.auth.aad.loginStrategy";
  protected static final String LOGIN_STRATEGY_UNIQUE = "Unique";
  protected static final String LOGIN_STRATEGY_PROVIDER_ID = "Same as Azure AD login";
  protected static final String LOGIN_STRATEGY_DEFAULT_VALUE = LOGIN_STRATEGY_UNIQUE;
  protected static final String MULTI_TENANT = "sonar.auth.aad.multiTenant";

  protected static final String CATEGORY = "Azure Active Directory";
  protected static final String SUBCATEGORY = "Authentication";
  protected static final String GROUPSYNCSUBCATEGORY = "Groups Synchronization";

  protected static final String LOGIN_URL = "https://login.microsoftonline.com";
  protected static final String LOGIN_URL_USGOV = "https://login.microsoftonline.us";
  protected static final String LOGIN_URL_DE = "https://login.microsoftonline.de";
  protected static final String LOGIN_URL_CN = "https://login.chinacloudapi.cn";
  protected static final String AUTHORIZATION_URL = "oauth2/authorize";
  protected static final String AUTHORITY_URL = "oauth2/token";
  protected static final String COMMON_URL = "common";

  protected static final String GRAPH_URL = "https://graph.microsoft.com";
  protected static final String GRAPH_URL_USGOV = "https://graph.microsoft.com";
  protected static final String GRAPH_URL_DE = "https://graph.microsoft.de";
  protected static final String GRAPH_URL_CN = "https://microsoftgraph.chinacloudapi.cn";
  protected static final String AUTH_REQUEST_FORMAT = "%s?client_id=%s&response_type=code&redirect_uri=%s&state=%s&scope=openid";
  protected static final String GROUPS_REQUEST_FORMAT = "/v1.0/%s/users/%s/transitiveMemberOf";

  private final Configuration config;

  public AadSettings(Configuration config) {
    this.config = config;
  }

  public static List<PropertyDefinition> definitions() {
    return Arrays.asList(
      PropertyDefinition.builder(ENABLED)
        .name("Enabled")
        .description("Enable Azure AD users to login. Value is ignored if client ID and secret are not defined.")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .type(BOOLEAN)
        .defaultValue(valueOf(false))
        .index(1)
        .build(),
      PropertyDefinition.builder(CLIENT_ID)
        .name("Client ID")
        .description("Client ID provided by Azure AD when registering the application.")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .index(2)
        .build(),
      PropertyDefinition.builder(CLIENT_SECRET)
        .name("Client Secret")
        .description("Client key provided by Azure AD when registering the application.")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .index(3)
        .build(),
      PropertyDefinition.builder(MULTI_TENANT)
        .name("Multi-tenant Azure Application")
        .description("multi-tenant application")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .type(BOOLEAN)
        .defaultValue(valueOf(false))
        .index(4)
        .build(),
      PropertyDefinition.builder(TENANT_ID)
        .name("Tenant ID")
        .description("Azure AD Tenant ID.")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .index(5)
        .build(),
      PropertyDefinition.builder(ALLOW_USERS_TO_SIGN_UP)
        .name("Allow users to sign-up")
        .description("Allow new users to authenticate. When set to 'false', only existing users will be able to authenticate to the server.")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .type(BOOLEAN)
        .defaultValue(valueOf(true))
        .index(6)
        .build(),
      PropertyDefinition.builder(LOGIN_STRATEGY)
        .name("Login generation strategy")
        .description(format("When the login strategy is set to '%s', the user's login will be auto-generated the first time so that it is unique. " +
            "When the login strategy is set to '%s', the user's login will be the Azure AD login.",
          LOGIN_STRATEGY_UNIQUE, LOGIN_STRATEGY_PROVIDER_ID))
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .type(SINGLE_SELECT_LIST)
        .defaultValue(LOGIN_STRATEGY_DEFAULT_VALUE)
        .options(LOGIN_STRATEGY_UNIQUE, LOGIN_STRATEGY_PROVIDER_ID)
        .index(7)
        .build(),
      PropertyDefinition.builder(DIRECTORY_LOCATION)
        .name("Directory Location")
        .description("The location of the Azure installation. You normally won't need to change this.")
        .category(CATEGORY)
        .subCategory(SUBCATEGORY)
        .type(SINGLE_SELECT_LIST)
        .defaultValue(DIRECTORY_LOC_GLOBAL)
        .options(DIRECTORY_LOC_GLOBAL, DIRECTORY_LOC_USGOV, DIRECTORY_LOC_DE, DIRECTORY_LOC_CN)
        .index(8)
        .build(),
      PropertyDefinition.builder(ENABLE_GROUPS_SYNC)
        .name("Enable Groups Synchronization")
        .description("Enable groups synchronization from Azure AD to SonarQube, For each Azure AD group user belongs to, the user will be associated to a group with the same name(if it exists) in SonarQube.")
        .category(CATEGORY)
        .subCategory(GROUPSYNCSUBCATEGORY)
        .type(BOOLEAN)
        .defaultValue(valueOf(false))
        .index(9)
        .build()
    );
  }

  public Optional<String> clientId() {
    return config.get(CLIENT_ID);
  }

  public boolean allowUsersToSignUp() {
    return config.getBoolean(ALLOW_USERS_TO_SIGN_UP).orElse(Boolean.TRUE);
  }

  public boolean enableGroupSync() {
    return config.getBoolean(ENABLE_GROUPS_SYNC).orElse(Boolean.FALSE);
  }

  public boolean multiTenant() {
    return config.getBoolean(MULTI_TENANT).orElse(Boolean.FALSE);
  }

  public Optional<String> tenantId() {
    return config.get(TENANT_ID);
  }

  public Optional<String> clientSecret() {
    return config.get(CLIENT_SECRET);
  }

  public boolean isEnabled() {
    return config.getBoolean(ENABLED).orElse(Boolean.FALSE) && clientId().isPresent() && clientSecret().isPresent() && loginStrategy().isPresent();
  }

  private String getEndpoint() {
    if (multiTenant()) {
      return COMMON_URL;
    } else {
      return tenantId().orElse("null");
    }
  }

  private String getLoginHost() {
    Optional<String> directoryLocation = config.get(DIRECTORY_LOCATION);

    if(directoryLocation.isPresent()) {
      switch (directoryLocation.get()) {
        case DIRECTORY_LOC_USGOV:
          return LOGIN_URL_USGOV;

        case DIRECTORY_LOC_DE:
          return LOGIN_URL_DE;

        case DIRECTORY_LOC_CN:
          return LOGIN_URL_CN;
      }
    }

    //This is the default "global" URL and will be returned if none of the special locations are selected.
    return LOGIN_URL;
  }

  public String authorizationUrl() {
    return String.format("%s/%s/%s", getLoginHost(), getEndpoint(), AUTHORIZATION_URL);
  }

  public String authorityUrl() {
    return String.format("%s/%s/%s", getLoginHost(), getEndpoint(), AUTHORITY_URL);
  }

  public String getGraphURL() {
    Optional<String> directoryLocation = config.get(DIRECTORY_LOCATION);

    if(directoryLocation.isPresent()) {
      switch (directoryLocation.get()) {
        case DIRECTORY_LOC_USGOV:
          return GRAPH_URL_USGOV;

        case DIRECTORY_LOC_DE:
          return GRAPH_URL_DE;

        case DIRECTORY_LOC_CN:
          return GRAPH_URL_CN;
      }
    }

    //This is the default "global" URL and will be returned if none of the special locations are selected.
    return GRAPH_URL;
  }

  public String getGraphMembershipUrl() {
    return getGraphURL() + GROUPS_REQUEST_FORMAT;
  }

  public Optional<String> loginStrategy() {
    return config.get(LOGIN_STRATEGY);
  }
}

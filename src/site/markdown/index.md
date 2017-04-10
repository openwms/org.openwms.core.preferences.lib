#Preferences

##Purpose
This microservice deals with configuration and preferences on application level. The
Configuration Service is more of a technical nature. This Preferences service may be used
to store preferences in different scopes. One scope may inherit Preferences from another
scope. In the current version, four different scopes are supported with the given
inheritance strategy:

| scope       | applied to | inherits from |
| ----------- | ---------- | ------------- |
| User        | the particular User only and overrides all other inherited Preferences (when not set to force) | Role |
| Role        | Preferences assigned to a particular Role. These Preferences are inherited to assigned Users, bit they may be overruled. | Module |
| Module      | These Preferences are defined on a specific software application module. The definition of the module boundaries is defined by the overall application. | application |
| Application | The application itself defined global used preferences. Each Preference may be configured to not be overridden, by default no one is overridden |


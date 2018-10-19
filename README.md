# OctoEvents
Kotlin app to receive from Github Webhook, infos about issues events

## What do I need to run it?
- Gradle
- Java JDK
- Postgresql

## How can I run it?
1. First, you have to create the  *octoevents_db* database in your postgresql
2. Now, you need to set some parameters in `main/kotlin/resources/application.properties` to became able to create a new webhook. The parameters are:
    - `hook.user` User to authenticate
    - `hook.password` Password to authenticate
    - `hook.repository_owner` The repository owner that you want to create a webhook
    - `hook.repository` The repository name that you want to create a webhook
    - `hook.payload_url` The payload url
2. In your console, run `gradle build`
3. Then, run `gradle bootRun`
4. It's done.

*(The dafault server port is 1010, you can change this and other things in application.properties file)*

*(Tip: Use https://ngrok.com/ to create a public url for you local host)*

*(Have you got doubts about payload url? check out https://developer.github.com/webhooks/#payloads)*

## How is it works?
Every time that an issue is assigned, unassigned, labeled, unlabeled, opened, edited, milestoned, demilestoned, closed, or reopened, the application will persist this events.
To see this events, you can use this endpoint http://localhost:1010/issues/{id}/events. Sending the issue id.

issues/{id}/events (response)
```
[
    {
        "action": "opened",
        "created_at": "Oct 18, 2018 11:04:47 AM"
    },
    {
        "action": "labeled",
        "created_at": "Oct 18, 2018 11:04:47 AM"
    },
    {
        "action": "closed",
        "created_at": "Oct 18, 2018 11:04:47 AM"
    }
]

```

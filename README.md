# OctoEvents
Kotlin app to receive from Github Webhook, infos about issues events

## What do I need to run it?
- Gradle
- Java JDK
- Postgresql

## How can I run it?
- First, you have to create the  *octoevents_db* database in your postgresql
- In your console, run `gradle build`
- Then, run `gradle bootRun`
- It's done. You can access http://localhost:1010 to check it

*(The dafault server port is 1010, you can change this and other things in application.properties file)*

## How is it works?
First of all, we need to create a webhook in a repository. Use this endpoint for it: http://localhost:1010/issues/init.
You have to use a POST request passing informations about authentication and the repository. This datas must be send it in the HTTP Header.

You post request will be something like this:
```
POST /issues/init HTTP/1.1
Host: localhost:1010
user: pedrinho
password: xxxxx
payload_url: http://7653cd54.ngrok.io
repository_owner: jonjts
repository: OctoEvents
Cache-Control: no-cache
Postman-Token: b78b663c-3753-ee82-cf7a-d5426d029591
```

*(Tip: Use https://ngrok.com/ to create a public url for you local host)*
*(Have you got doubts about payload url? check it out https://developer.github.com/webhooks/#payloads)*

Now, every time that an issue is assigned, unassigned, labeled, unlabeled, opened, edited, milestoned, demilestoned, closed, or reopened, the application will perciste this events.
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

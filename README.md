# StarCreations Utils Bot (README WORK IN PROGRESS)

## Description
A Discord Bot made with <3 by Antostarwars for [StarCreations](https://discord.starcreations.it/)

## Features
List of all the features in the Bot

- Ticket System
    - Dropdown Menu for Categories
    - Ticket Logs and Transcript
    - Close command with Reason
    - Close button
- Affiliation System
    - Affiliate Code
    - Affiliate Earnings
- User Profile
    - Command to lookup for a specific user
- Giveaway System
    - Start Giveaway
    - Reroll
    - End Giveaway
    - Members with a specific role will have 2x entry (More possibility to win)
- Blacklist System
    - Remove Access to all Bot features
    - Remove permission to write/join Channels (Text and Voice)

## Request Features
You can request a new feature using this two methods:
- Suggestions Channel in StarCreations [Discord Server](https://discord.starcreations.it/)

- Email at [hello@antostarwars,xyz](mailto:hello@antostarwars.xyz)

# How to self host

1) Install Java 17 JDK and Docker Engine

```bash
    sudo apt install openjdk-17
    sudo apt install docker
```

2) Clone this repo
```bash
    git clone <Link of this repo>
```

3) Edit .env file (Check .env.example)
```bash
    nano .env
```

5) Go in the Directory of the Project and run the docker compose
```bash
    cd StarCreationsUtils
    docker compose up -d Discord-Bot # -d will start the container in background
```
**Amazing!** Now you'll have your discord bot hosted!


## Technology Used

**Storage**:
- Json
- MongoDB

**Discord Gateway**:
- JDA (Java Discord API)

**Programming Language**:
- Java

**Hosting Solution**:
- Docker
- Docker Compose

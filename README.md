# OPass Compose

Status: **Work In Progress**

## Feature

- [x] Home
- [x] Switch events
- [ ] Sessions
  - [x] Browse
  - [ ] Details
- [ ] Ticket
  - [x] Enter manually
  - [ ] Scan a QR Code
  - [ ] Scan a image
  - [ ] Show my ticket
- [ ] WebView
- [ ] Settings
  - [ ] Save tokens
  - [ ] Local cache
- [ ] Material Design 3 theme

## Screenshot

![home](images/home.png)
![switch-event](images/switch-event.png)
![schedule](images/schedule.png)

## Development

### Format

```sh
./gradlew :app:ktfmtFormat
```

### Lint

```sh
./gradlew :app:detekt
```

### Local debug

#### Requirements

- ADB
- Caddy

#### Start a mock server

```sh
adb reverse tcp:8080 tcp:8080
```

```sh
cd local-server
caddy run --watch
```

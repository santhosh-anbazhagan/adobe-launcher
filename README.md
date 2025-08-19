# Adobe Launcher Plugin

A Capacitor plugin for launching Adobe applications and opening URLs on mobile devices.

## Install

```bash
npm install adobe-launcher-plugin
npx cap sync
```

## API

### canOpenUrl(options: { url: string })

Check if a URL can be opened on the device. Returns an object with `value` (boolean) and optional `error` (string).

```typescript
import { AdobeLauncher } from 'adobe-launcher-plugin';

// Check regular URL
const result = await AdobeLauncher.canOpenUrl({ url: 'https://example.com' });
if (result.error) {
  console.error('Error:', result.error);
} else {
  console.log('Can open URL:', result.value);
}

// Check Adobe Scan URL
const adobeResult = await AdobeLauncher.canOpenUrl({ url: 'adobe://scan' });
if (adobeResult.error) {
  console.error('Error:', adobeResult.error);
} else {
  console.log('Can open Adobe Scan:', adobeResult.value);
}
```

### canOpenApp(options: { packageName?: string })

Check if a specific app is installed on the device. Returns an object with `value` (boolean).

```typescript
import { AdobeLauncher } from 'adobe-launcher-plugin';

// Check if Adobe Scan is installed (default)
const result = await AdobeLauncher.canOpenApp();
console.log('Adobe Scan installed:', result.value);

// Check if specific app is installed
const appResult = await AdobeLauncher.canOpenApp({ 
  packageName: 'com.example.app' 
});
console.log('App installed:', appResult.value);
```

### openUrl(options: { url: string; mimeType?: string })

Open a URL on the device.

```typescript
import { AdobeLauncher } from 'adobe-launcher-plugin';

const result = await AdobeLauncher.openUrl({ 
  url: 'https://example.com',
  mimeType: 'text/html' // optional
});
console.log('URL opened successfully:', result.value);
```

### openAdobeScan(options?: { url?: string })

Launch the Adobe Scan application if installed. Optionally specify a custom URL scheme.

```typescript
import { AdobeLauncher } from 'adobe-launcher-plugin';

// Launch with default Adobe Scan URL
const result = await AdobeLauncher.openAdobeScan();
if (result.value) {
  console.log('Adobe Scan launched successfully');
} else {
  console.log('Adobe Scan is not installed');
}

// Launch with custom URL
const customResult = await AdobeLauncher.openAdobeScan({ 
  url: 'adobescan://custom-action' 
});
```

### openApp(options: { packageName: string; mimeType?: string; action?: string; data?: string })

Launch any application by package name with optional parameters.

```typescript
import { AdobeLauncher } from 'adobe-launcher-plugin';

// Launch app with basic parameters
const result = await AdobeLauncher.openApp({ 
  packageName: 'com.example.app' 
});

// Launch app with full parameters
const fullResult = await AdobeLauncher.openApp({
  packageName: 'com.example.app',
  mimeType: 'text/plain',
  action: 'android.intent.action.VIEW',
  data: 'https://example.com'
});

if (result.success) {
  console.log('App launched successfully');
} else {
  console.log('Failed to launch app');
}
```

## Platform Support

- **Android**: Full support for all methods
- **iOS**: Full support for all methods  
- **Web**: Methods return false with console warnings (not supported on web)

## Permissions

### Android
- `QUERY_ALL_PACKAGES`: Required to check if apps can handle specific URLs

### iOS
- No additional permissions required beyond standard URL handling capabilities

## Usage Examples

### Check and Open URLs

```typescript
import { AdobeLauncher } from 'adobe-launcher-plugin';

async function openWebsite(url: string) {
  try {
    // Check if URL can be opened
    const canOpen = await AdobeLauncher.canOpenUrl({ url });
    
    if (canOpen.value) {
      // Open the URL
      const result = await AdobeLauncher.openUrl({ url });
      if (result.value) {
        console.log('Website opened successfully');
      }
    } else {
      console.log('Cannot open this URL');
    }
  } catch (error) {
    console.error('Error:', error);
  }
}

// Usage
openWebsite('https://www.adobe.com');
```

### Launch Adobe Scan

```typescript
import { AdobeLauncher } from 'adobe-launcher-plugin';

async function launchAdobeScan() {
  try {
    const result = await AdobeLauncher.openAdobeScan();
    
    if (result.value) {
      console.log('Adobe Scan launched successfully');
    } else {
      console.log('Adobe Scan is not installed');
      // You could redirect to app store here
    }
  } catch (error) {
    console.error('Error launching Adobe Scan:', error);
  }
}
```

## Development

### Building

```bash
npm run build
```

### Testing

```bash
npm run test
```

### Sync with Capacitor

```bash
npx cap sync
```

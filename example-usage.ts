import { AdobeLauncher } from './src/index';

// Example 1: Check if a URL can be opened
async function checkUrl(): Promise<void> {
  try {
    const result = await AdobeLauncher.canOpenUrl({ 
      url: 'https://www.adobe.com' 
    });
    
    if (result.error) {
      console.error('Error checking URL:', result.error);
    } else {
      console.log('Can open Adobe website:', result.value);
    }
  } catch (error) {
    console.error('Error checking URL:', error);
  }
}

// Example 1b: Check Adobe Scan URL
async function checkAdobe(): Promise<void> {
  try {
    const result = await AdobeLauncher.canOpenUrl({ url: 'adobe://scan' });
    
    if (result.error) {
      console.error('Error checking Adobe Scan:', result.error);
    } else {
      console.log('Can open Adobe Scan:', result.value);
    }
  } catch (error) {
    console.error('Error checking Adobe Scan:', error);
  }
}

// Example 1c: Check if app is installed
async function checkAppInstalled(): Promise<void> {
  try {
    // Check if Adobe Scan is installed (default)
    const result = await AdobeLauncher.canOpenApp({});
    console.log('Adobe Scan installed:', result.value);
    
    // Check if specific app is installed
    const appResult = await AdobeLauncher.canOpenApp({ 
      packageName: 'com.example.app' 
    });
    console.log('Example app installed:', appResult.value);
  } catch (error) {
    console.error('Error checking app installation:', error);
  }
}

// Example 2: Open a URL
async function openWebsite(): Promise<void> {
  try {
    const result = await AdobeLauncher.openUrl({ 
      url: 'https://www.adobe.com',
      mimeType: 'text/html'
    });
    console.log('Website opened successfully:', result.value);
  } catch (error) {
    console.error('Error opening website:', error);
  }
}

// Example 3: Launch Adobe Scan
async function launchAdobeScan(): Promise<void> {
  try {
    const result = await AdobeLauncher.openAdobeScan();
    if (result.value) {
      console.log('Adobe Scan launched successfully');
    } else {
      console.log('Adobe Scan is not installed');
    }
  } catch (error) {
    console.error('Error launching Adobe Scan:', error);
  }
}

// Example 3b: Launch Adobe Scan with custom URL
async function launchAdobeScanCustom(): Promise<void> {
  try {
    const result = await AdobeLauncher.openAdobeScan({ 
      url: 'adobescan://custom-action' 
    });
    if (result.value) {
      console.log('Adobe Scan launched with custom URL successfully');
    } else {
      console.log('Adobe Scan custom URL not supported');
    }
  } catch (error) {
    console.error('Error launching Adobe Scan with custom URL:', error);
  }
}

// Example 4: Launch any app
async function launchApp(): Promise<void> {
  try {
    const result = await AdobeLauncher.openApp({ 
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
  } catch (error) {
    console.error('Error launching app:', error);
  }
}

// Example 5: Complete workflow - check and open URL
async function completeWorkflow(): Promise<void> {
  try {
    const url = 'https://www.adobe.com';
    
    // First check if we can open the URL
    const canOpen = await AdobeLauncher.canOpenUrl({ url });
    
    if (canOpen.value) {
      console.log('URL can be opened, proceeding...');
      
      // Then open the URL
      const openResult = await AdobeLauncher.openUrl({ url });
      if (openResult.value) {
        console.log('URL opened successfully');
      } else {
        console.log('Failed to open URL');
      }
    } else {
      console.log('Cannot open this URL');
    }
  } catch (error) {
    console.error('Workflow error:', error);
  }
}

// Export functions for use in other files
export {
  checkUrl,
  checkAdobe,
  checkAppInstalled,
  openWebsite,
  launchAdobeScan,
  launchAdobeScanCustom,
  launchApp,
  completeWorkflow
};

// Example usage in a React component or other context:
/*
import React, { useState } from 'react';
import { AdobeLauncher } from 'adobe-launcher-plugin';

function AdobeLauncherComponent() {
  const [status, setStatus] = useState('');

  const handleOpenAdobeScan = async () => {
    try {
      setStatus('Launching Adobe Scan...');
      const result = await AdobeLauncher.openAdobeScan();
      
      if (result.value) {
        setStatus('Adobe Scan launched successfully!');
      } else {
        setStatus('Adobe Scan is not installed');
      }
    } catch (error) {
      setStatus(`Error: ${error.message}`);
    }
  };

  const handleOpenWebsite = async () => {
    try {
      setStatus('Opening website...');
      const result = await AdobeLauncher.openUrl({ 
        url: 'https://www.adobe.com' 
      });
      
      if (result.value) {
        setStatus('Website opened successfully!');
      } else {
        setStatus('Failed to open website');
      }
    } catch (error) {
      setStatus(`Error: ${error.message}`);
    }
  };

  return (
    <div>
      <h2>Adobe Launcher Plugin Demo</h2>
      <button onClick={handleOpenAdobeScan}>
        Launch Adobe Scan
      </button>
      <button onClick={handleOpenWebsite}>
        Open Adobe Website
      </button>
      <p>Status: {status}</p>
    </div>
  );
}

export default AdobeLauncherComponent;
*/

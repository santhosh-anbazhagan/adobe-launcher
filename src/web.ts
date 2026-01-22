import { WebPlugin } from '@capacitor/core';

import type { AdobeLauncherPlugin, FilePickerResult } from './definitions';

export class AdobeLauncherWeb extends WebPlugin implements AdobeLauncherPlugin {
  async canOpenUrl(): Promise<{ value: boolean; error?: string }> {
    console.warn('AdobeLauncher: canOpenUrl not supported on web');
    return { value: false, error: 'Not supported on web platform' };
  }

  async canOpenApp(): Promise<{ value: boolean }> {
    console.warn('AdobeLauncher: canOpenApp not supported on web');
    return { value: false };
  }

  async openUrl(): Promise<{ value: boolean }> {
    console.warn('AdobeLauncher: openUrl not supported on web');
    return { value: false };
  }

  async openAdobeScan(): Promise<{ value: boolean }> {
    console.warn('AdobeLauncher: openAdobeScan not supported on web');
    return { value: false };
  }

  async openApp(): Promise<{ success: boolean }> {
    console.warn('AdobeLauncher: openApp not supported on web');
    return { success: false };
  }

  async openApp2(): Promise<{ success: boolean }> {
    console.warn('AdobeLauncher: openApp2 not supported on web');
    return { success: false };
  }

  async pickFile(): Promise<FilePickerResult> {
    console.warn('AdobeLauncher: pickFile not supported on web');
    // Return a mock result for web development
    return {
      uri: `content://mock/document_${Date.now()}.pdf`,
      name: `Document_${Date.now()}.pdf`,
      size: 1024 * 1024, // 1MB mock size
      mimeType: 'application/pdf'
    };
  }
}

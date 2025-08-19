import { WebPlugin } from '@capacitor/core';

import type { AdobeLauncherPlugin } from './definitions';

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
}

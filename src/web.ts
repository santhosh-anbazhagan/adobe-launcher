import { WebPlugin } from '@capacitor/core';

import type { AdobeLauncherPlugin } from './definitions';

export class AdobeLauncherWeb extends WebPlugin implements AdobeLauncherPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}

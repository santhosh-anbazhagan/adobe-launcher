import { registerPlugin } from '@capacitor/core';

import type { AdobeLauncherPlugin } from './definitions';

const AdobeLauncher = registerPlugin<AdobeLauncherPlugin>('AdobeLauncher', {
  web: () => import('./web').then((m) => new m.AdobeLauncherWeb()),
});

export * from './definitions';
export { AdobeLauncher };

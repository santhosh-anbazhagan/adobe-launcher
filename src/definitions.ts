export interface AdobeLauncherPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}

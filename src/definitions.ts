export interface FilePickerResult {
  uri: string;
  name: string;
  size: number;
  mimeType: string;
}

export interface AdobeLauncherPlugin {
  canOpenUrl(options: { url: string }): Promise<{ value: boolean; error?: string }>;
  canOpenApp(options: { packageName?: string }): Promise<{ value: boolean }>;
  openUrl(options: { url: string; mimeType?: string }): Promise<{ value: boolean }>;
  openAdobeScan(options?: { url?: string }): Promise<{ value: boolean }>;
  openApp(options: { packageName: string; mimeType?: string; action?: string; data?: string }): Promise<{ success: boolean }>;
  openApp2(options: { packageName: string }): Promise<{ success: boolean }>;
  pickFile(): Promise<FilePickerResult>;
}

declare interface Result<T = any> {
  data?: T;
  success: boolean;
  code: number;
  message?: string;
}

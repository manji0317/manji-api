
declare interface Login {
  username: string;
  password: string;
}

declare interface Token {
  accessToken: string;
  refreshToken: string;
}

declare interface User {
  id: number;
  username: string;
  email: string;
  phone: string;
  avatar: string;
}

declare interface Auth  {
  isAuth: boolean;
  userInfo: User | null;
}

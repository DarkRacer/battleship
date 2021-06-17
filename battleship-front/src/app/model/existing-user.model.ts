export interface ExistingUser {
  id: string;
  userName: string;
  picture: string;
  status: string;
  win: number;
  lost: number;
  provider: string;
  userId: string;
  token?: string;
  role?: string;
  createdAt?: string;
  createdBy?: number;
}

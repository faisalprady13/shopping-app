export type Screen = 'start' | 'lists' | 'details' | 'add'

export type ShoppingItem = {
  id: string,
  name: string,
  quantity: string,
  status: Status
};

export type ShoppingList = {
  id: string;
  name: string;
  date: string;
  products: ShoppingItem[];
};

export type User = {
  id: string
};

export type UserDto = {
  name: string
};

export type AuthProvider = 'LOCAL' | 'GOOGLE' | 'GITHUB'
export type Role = 'USER' | 'ADMIN'

export type SafeUser = {
  id: string
  name: string
  email: string
  authProvider: AuthProvider
  role: Role
}

export type RegisterDto = {
  name: string
  email: string
  password: string
}

export type LoginDto = {
  email: string
  password: string
}

export type ShoppingListDto = {
  name: string,
  user: User
};

export const Status = {
  OPEN: 'OPEN',
  CLOSED: 'CLOSED',
} as const

export type Status = typeof Status[keyof typeof Status]

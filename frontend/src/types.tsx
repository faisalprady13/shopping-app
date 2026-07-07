export type Screen = 'start' | 'lists' | 'details' | 'add'

export type ShoppingItem = {
  id: string;
  name: string;
  quantity: string;
  status: Status;
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

export type ShoppingListDto = {
  name: string,
  user: User
};

// @ts-ignore
export enum Status {
  OPEN='OPEN',
  CLOSED='CLOSED',
}
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { SessionService } from './session.service';

class SessionInformationImpl implements SessionInformation {
  token: string;
  type: string;
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  admin: boolean;

  constructor(token: string, type: string, id: number, username: string, firstName: string, lastName: string, admin: boolean) {
    this.token = token;
    this.type = type;
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.admin = admin;
  }
}

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should not be logged by default', () => {
    expect(service.isLogged).toBeFalsy();
  });

  it('should log in', () => {
    let user = new SessionInformationImpl("", "", 1, "testuser", "test", "user", true);
    service.logIn(user);
    expect(service.isLogged).toBeTruthy();
  })

  it('should logout in', () => {
    let user = new SessionInformationImpl("", "", 1, "testuser", "test", "user", true);
    service.logIn(user);
    expect(service.isLogged).toBeTruthy();
    service.logOut();
    expect(service.isLogged).toBeFalsy();
  })
});

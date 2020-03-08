import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

export function jwtOptionsFactory(jwtConfig: JwtConfig) {
  return {
    tokenGetter: () => localStorage.getItem('accessToken'),
    whitelistedDomains: jwtConfig.whitelistedDomains,
    blacklistedRoutes: jwtConfig.blacklistedRoutes,
    skipWhenExpired: false
  };
}

@Injectable({ providedIn: 'root' })
export class JwtConfig {
  public blacklistedRoutes: (string | RegExp)[];
  public whitelistedDomains: string[];

  constructor() {
    this.blacklistedRoutes = environment.jwt.blacklistedRoutes;
    this.whitelistedDomains = environment.jwt.whitelistedDomains;
  }
}

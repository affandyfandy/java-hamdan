import { Component } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faUser } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [FaIconComponent],
  templateUrl: './header.component.html'
})
export class HeaderComponent {
  faUser = faUser;
}

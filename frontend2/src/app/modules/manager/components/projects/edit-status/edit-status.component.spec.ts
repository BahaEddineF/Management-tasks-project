import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditStatusComponent } from './edit-status.component';

describe('EditStatusComponent', () => {
  let component: EditStatusComponent;
  let fixture: ComponentFixture<EditStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditStatusComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
